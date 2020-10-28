
import java.io.DataInputStream;
import java.io.EOFException;

import lejos.hardware.Bluetooth;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Receiver { // This is the receiver

	public static void main(String[] args) throws Exception {
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 50).offset(-50);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 50).offset(50);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot pilot = new MovePilot(chassis);
		pilot.setLinearSpeed(pilot.getMaxLinearSpeed());
		pilot.setAngularSpeed(pilot.getMaxAngularSpeed());

		NXTCommConnector connector = Bluetooth.getNXTCommConnector();

		System.out.println("Waiting for connection ...");
		NXTConnection con = connector.waitForConnection(0, NXTConnection.RAW);
		System.out.println("Connected");
		Button.LEDPattern(1);
		LCD.clear();

		DataInputStream dis = con.openDataInputStream();

		byte[] n = new byte[1];

		while (true) {
			try {
				if (dis.read(n) == -1)
					break;
			} catch (EOFException e) {
				break;
			}

			if (n[0] == 1) {
				pilot.forward();
			} else if (n[0] == 4) {
				pilot.backward();
			} else if (n[0] == 0) {
				pilot.stop();
			} else if (n[0] == 8) {
				pilot.arc(0, 360, true);
			} else if (n[0] == 16) {
				pilot.arc(0, -360, true);
			} else if (n[0] == 0) {
				pilot.stop();
			} else if (n[0] == 2) {
				Button.LEDPattern(3);
				Sound.playNote(Sound.FLUTE, 44, 2000);
				pilot.arc(0, 360);
				Button.LEDPattern(1);
			} else if (n[0] == 32) {
				break;
			}

		}

		dis.close();
		con.close();

	}

}
