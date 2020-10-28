import java.io.DataOutputStream;


import lejos.hardware.Bluetooth;
import lejos.hardware.Button;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;

public class Sender { // This is the sender
	private static final String EV3 = "00:16:53:44:68:8B";

	public static void main(String[] args) throws Exception {

		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		System.out.println("Connecting to " + EV3);

		NXTConnection connection = connector.connect(EV3, NXTConnection.RAW);
		if (connection == null) {
			Button.LEDPattern(2);
			System.out.print("No Connection");
			System.out.print("Exiting Now");
			Delay.msDelay(1000);
			System.exit(0);
		}

		System.out.println("Connected");

		DataOutputStream output = connection.openDataOutputStream();
		Button.LEDPattern(1);

		System.out.println("Sending data");
		int cnt = 0;
		byte[] track = new byte[1];
		while (Button.getButtons() != Button.ID_ESCAPE) {
			int button = Button.waitForAnyEvent();
			track[0] = (byte) button;
			output.write(track);
			output.flush();

			if (track[0] == 1) {
				System.out.println("Moving Forward");
			} else if (track[0] == 4) {
				System.out.println("Moving Backwards");
			} else if (track[0] == 8) {
				System.out.println("Turning Right");
			} else if (track[0] == 16) {
				System.out.println("Turning Left");
			}
		}
		System.out.println("All data sent");

		output.close();
		connection.close();

		System.out.println("Connection closed");
	}
}
