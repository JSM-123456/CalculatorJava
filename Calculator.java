
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Calculator {
	
	private JFrame frame;
	private JTextField textField;
	private JLabel label;
	private String currentInput = "";
	private String operator = "";
	private double firstNum = 0;
	private boolean isDecimal = false;

	public static void main(String[] args) {
		// Calculator 객체 생성 후 실행
		// EventQueue.invokeLater는 GUI업데이트를 안전하게 처리하는 방법임
		EventQueue.invokeLater(() -> {
			try {
				Calculator window = new Calculator();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}
	
	// 생성자에서 GUI 구성
	public Calculator() {
		initialize();
	}
	
	// GUI 초기화 메소드
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Calculator");
		frame.setBounds(100, 100, 400, 500); // 창의 크기를 (400, 500)로 설정한다
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout()); // 레이아웃을 BorderLayout으로 설정한다
		
		label = new JLabel("JSM's Calculator", SwingConstants.LEFT);
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setForeground(Color.GREEN);
		label.setBackground(Color.BLACK);
		label.setOpaque(true);
		frame.getContentPane().add(label, BorderLayout.NORTH);
		
		// 텍스트 필드 : 사용자 입력 표시
		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.BOLD, 60));
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setBackground(Color.BLACK);
		textField.setColumns(10);		
		textField.setForeground(Color.WHITE);
		textField.setSize(10, 500);
		textField.setBorder(new LineBorder(Color.BLACK, 1)); // 텍스트 필드 테두리 색, 두께 지정
		frame.getContentPane().add(textField, BorderLayout.CENTER);

		// 버튼 패널
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 4, 10, 10)); // 5 x 4 격자구조에 버튼 사이의 간격은 10으로 설정
		panel.setBackground(Color.BLACK);
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		// 숫자와 연산자 버튼들
		String[] buttons = {
				 "7", "8", "9", "/",
		            "4", "5", "6", "x",
		            "1", "2", "3", "-",
		            ".", "0", "=", "+",
		            "C"
		};
		
		// 버튼 생성 및 추가
		for (String text : buttons) {
			JButton button = new JButton(text);
			button.setFont(new Font("Arial", Font.PLAIN, 24));
			button.setBackground(Color.GRAY);
			button.setForeground(Color.WHITE);
			button.addActionListener(new ButtonClickListener());
			panel.add(button);
		}
	}
	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			
			if (command.charAt(0) == '=') {
				// "=" 버튼 클릭 시 계산 수행
				calculate();
			} else if (command.equals("/") || command.equals("x") || command.equals("-") || command.equals("+")) {
				// 연산자 버튼 클릭 시 처리
				if (!currentInput.isEmpty()) {
					if (!operator.isEmpty()) {
		                calculate();  
		            }
					// currentInput을 firstNum으로 지정하고 이어서 계산하는 처리를 하였다.
		            firstNum = Double.parseDouble(currentInput); // 다음 계산을 위한 firstNum 세팅
		            operator = command; // 연산자 세팅
		            textField.setText(currentInput + " " + operator + " ");
		            currentInput = ""; // 다음 숫자를 위한 currentInput 비우기
			    }
			} else if (command.equals("C")) {
				currentInput = "";
				operator = "";
				firstNum = 0;
				textField.setText("");
			} else {
				// 숫자 또는 점 버튼 클릭 시 처리
				if (command.equals(".")) {
					if(!isDecimal) {
						currentInput += command;
						isDecimal = true;
					}
				}
				currentInput += command;
				textField.setText(currentInput);
			}
		}
	}
	// 계산 메서드
	private void calculate() {
		if (operator.isEmpty() || currentInput.isEmpty()) return;
		
		double secondNum = Double.parseDouble(currentInput);
		double result = 0;
		
		
		if (isDecimal || currentInput.contains(".")) {
			// 실수 계산
			switch (operator) {
			case "+" : result = firstNum + secondNum; break;
			case "-" : result = firstNum - secondNum; break;
			case "x" : result = firstNum * secondNum; break;
			case "/" :
				if (secondNum != 0) {
					result = firstNum / secondNum;
				} else {
				textField.setText("Error");
				return;
				}
				break;
			}
		} else {
			// 정수 계산
			int intFirstNum = (int) firstNum;
			int intSecondNum = (int) secondNum;
			switch (operator) {
				case "+" : result = intFirstNum + intSecondNum; break;
				case "-" : result = intFirstNum - intSecondNum; break;
				case "x" : result = intFirstNum * intSecondNum; break;
				case "/" :
					while (intSecondNum != 0) {
						result = intFirstNum / intSecondNum;
					 if ((intFirstNum % intSecondNum) != 0){
						result = (double) intFirstNum / (double) intSecondNum ;
					} else {
						textField.setText("Error");
						return;
					}
					break;
				}
	        }
		}
		if (result == (int) result) {
			textField.setText(String.valueOf((int) result));
		} else {
			double result_format = Math.round(result * 1000.0) / 1000.0;
			textField.setText(String.valueOf(result_format));
		}
		
		// textField.setText(firstNum + " " + operator + " " + secondNum + "=" + result);
        currentInput = String.valueOf(result);  // 계산된 값은 결과로 표시
        firstNum = result;
        operator = "";
        isDecimal = false; // 실수 계산 여부 초기화
	}

}
