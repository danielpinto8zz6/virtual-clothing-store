package com.sample;

import com.sample.view.MainJFrame;

public class App {

	public static final void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainJFrame().setVisible(true);
			}
		});
	}
}