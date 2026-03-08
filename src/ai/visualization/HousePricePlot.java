package ai.visualization;

import ai.model.House;
import ai.regression.LinearRegressor;
import ai.util.FileUtil;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HousePricePlot extends JPanel {
	private List<House> houses;
	private double w;
	private double b;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//坐标轴
		g.setColor(Color.BLACK);
		g.fillOval(50-4, 550-4, 8, 8);
		xLine(g, 550);
		yLine(g, 50);
		for (int i=0; i<=(600/25); i++) {
			g.drawLine(i*25, 550, i*25, 548);
			g.drawLine(50, i*25, 52, i*25);
		}
		
		//点
		g.setColor(Color.RED);
		for (House h : houses) {
			g.fillOval(toX(h.getArea())-4, toY(h.getPrice())-4, 8, 8);
		}
		
		//回归直线y=wx+b
		double x = -b/w;
		double y = b;
		g.setColor(Color.BLUE);
		extendLine(g, toX(x), 550, toY(y), 50);
	}
	
	private int toX(double area) {return 50 + (int)(area * 25 / 10);}
	private int toY(double price) {return 550 - (int)(price * 25 / 30);}
	private void xLine(Graphics g, int y) {
		g.drawLine(0, y, getWidth(), y);
	}
	private void yLine(Graphics g, int x) {
		g.drawLine(x, 0, x, getHeight());
	}
	private void extendLine(Graphics g, int x1, int y1, int x2, int y2) {
		int width = getWidth();
		int height = getHeight();
		if (x1 == x2) {
			g.drawLine(x1, 0, x1, height);
			return;
		}
		
		double k = (y2 - y1) / (x2 - x1);
		double b = y1 - (k * x1);
		
		int xLeft = 0, yLeft = (int)((k * xLeft) + b);
		int xRight = width, yRight = (int)((k * xRight) + b);
		int yUp = 0, xUp = (int)((yUp - b) / k);
		int yDown = height, xDown = (int)((yDown - b) / k);
		int[][] bound = {{xLeft, yLeft},
				         {xRight, yRight},
				         {xUp, yUp},
				         {xDown, yDown}};
		boolean[] onBound = new boolean[4];
		for (int i=0; i<4; i++) {
			onBound[i] = false;
		}
		int[][] point = new int[2][2];
		int n = 0;
		
		if (yLeft > 0 && yLeft <= height) {onBound[0] = true;}
		if (yRight >= 0 && yRight < height) {onBound[1] = true;}
		if (xUp >= 0 && xUp < width) {onBound[2] = true;}
		if (xDown > 0 && xDown <= width) {onBound[3] = true;}
		
		for (int i=0; i<4; i++) {
			if (onBound[i]) {
				point[n][0] = bound[i][0];
				point[n][1] = bound[i][1];
				n++;
			}
		}
		
		g.drawLine(point[0][0], point[0][1], point[1][0], point[1][1]);
	}
	
	public HousePricePlot(List<House> houses, double w, double b) {
		this.houses = houses;
		this.w = w;
		this.b = b;
		this.setBackground(Color.WHITE);
	}
	
	public static void main(String[] args) {
		List<House> samples = FileUtil.loadHouse("data/houses.txt");
		LinearRegressor lr = new LinearRegressor(samples);
		lr.train(0.0001, 1000);
		
		JFrame frame = new JFrame("房价可视化");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 635);
		
		HousePricePlot plot = new HousePricePlot(samples, lr.getW(), lr.getB());
		plot.setBackground(Color.WHITE);
		
		frame.add(plot);
		frame.setVisible(true);
	}
}
