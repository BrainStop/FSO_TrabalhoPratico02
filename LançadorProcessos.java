package tp2;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

public class LançadorProcessos extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Object[]> processList = new ArrayList<Object[]>();
	private JPanel contentPane;
	private JTextField textField;
	private String path;
	private boolean isDebugAtivo = false;
	private JTextField textFieldLog;
	private JTextField processNBox;
	private int nProcessos = 0;
	private long startTime;
	private long actualTime;
	private String javaExe = "JAVA";

	public void estadoProcessos() {

		for (Object[] p : processList) {
			if (((Process) p[0]).isAlive()) {
			} else {
				processList.remove(p);
				actualizarLog("processo " + p[1] + " terminado");
				--nProcessos;

			}
		}
	}

	protected void actualizarLog(String mensagem) {

		if (isDebugAtivo) {
			startTime = System.currentTimeMillis();
			textFieldLog.setText(mensagem);

		}
	}

	public void lançarProcessoExe(String path) {
		ProcessBuilder pb = new ProcessBuilder(path);
		Process p = null;
		String[] pathProcesso = path.split("\\\\");
		String nomeProcesso = pathProcesso[pathProcesso.length - 1];

		try {
			p = pb.start();
			processList.add(new Object[] { p, nomeProcesso });
			actualizarLog("Iniciado o processo " + nomeProcesso);
			++nProcessos;
		} catch (IOException e) {
			actualizarLog(e.getMessage());
			System.err.println(e.getMessage());
		}

	}

	public void lançarProcessoJava(String path) {

		// maneira de executar a partir do Java um processo JAVA
		Process p = null;
		String[] pathProcesso = path.split("\\\\");
		String nomeProcesso = pathProcesso[pathProcesso.length - 1];
		System.out.println("java -jar "+path);

		try {
			p = Runtime.getRuntime().exec("java -jar "+path);
			processList.add(new Object[] { p, nomeProcesso });
			actualizarLog("Iniciado o processo " + nomeProcesso);
		} catch (IOException e) {
			actualizarLog(e.getMessage());
			System.err.println(e.getMessage());
		}

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		System.out.println("Programa LançadorProcessos Iniciado.");

		LançadorProcessos l = new LançadorProcessos();
		l.setVisible(true);
		l.run();

	}

	/**
	 * Create the frame.
	 */
	public LançadorProcessos() {
		System.out.println("Interface a ser criada.");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 433);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		ImageIcon start = new ImageIcon(getClass().getResource("doge.png"));
		JButton execBtn = new JButton(start);

		execBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(javaExe.equals("JAVA")){
					lançarProcessoJava(path);

				}else{
					lançarProcessoExe(path);

				}
			}
		});
		execBtn.setBackground(Color.RED);
		execBtn.setBounds(62, 68, 311, 234);
		contentPane.add(execBtn);

		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				path = textField.getText();

			}
		});
		textField.setBounds(73, 37, 340, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JTextPane txtpnProcesso = new JTextPane();
		txtpnProcesso.setBackground(SystemColor.control);
		txtpnProcesso.setText("Processo");
		txtpnProcesso.setBounds(6, 37, 49, 20);
		contentPane.add(txtpnProcesso);

		textFieldLog = new JTextField();
		textFieldLog.setEditable(false);
		textFieldLog.setBounds(84, 344, 340, 20);
		contentPane.add(textFieldLog);

		processNBox = new JTextField();
		processNBox.setEditable(false);
		processNBox.setBounds(207, 313, 26, 20);
		contentPane.add(processNBox);

		JTextPane txtpnNDeProcessos = new JTextPane();
		txtpnNDeProcessos.setText("N\u00BA Processos Activos");
		txtpnNDeProcessos.setBackground(SystemColor.menu);
		txtpnNDeProcessos.setBounds(84, 313, 113, 20);
		contentPane.add(txtpnNDeProcessos);
		processNBox.setText(Integer.toString(nProcessos));

		JTextPane txtpnLog = new JTextPane();
		txtpnLog.setText("Log");
		txtpnLog.setBackground(SystemColor.menu);
		txtpnLog.setBounds(48, 344, 26, 20);
		contentPane.add(txtpnLog);

		JCheckBox debugBox = new JCheckBox("Debug");
		debugBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (isDebugAtivo) {
					isDebugAtivo = false;
					actualizarLog("");
				} else {
					isDebugAtivo = true;
				}
			}
		});
		debugBox.setBounds(6, 311, 72, 23);
		contentPane.add(debugBox);

		JToggleButton tglbtnJavaexe = new JToggleButton("JAVA/EXE");
		tglbtnJavaexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(javaExe.equals("JAVA")){
					javaExe = "EXE";
					actualizarLog("Modo EXE");

				}else{
					javaExe = "JAVA";
					actualizarLog("Modo JAVA");

				}
			}
		});
		tglbtnJavaexe.setBounds(292, 313, 121, 23);
		contentPane.add(tglbtnJavaexe);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (;;) {
			actualTime = System.currentTimeMillis();
			if (actualTime - startTime >= 5000) {
				actualizarLog("");
			}

			try {
				Thread.sleep(1000);
				estadoProcessos();

			} catch (Exception e) {

			}
		}
	}
}
