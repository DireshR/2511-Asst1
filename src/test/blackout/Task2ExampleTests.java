package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
        @Test
        public void testEntitiesInRange() {
                // Task 2
                // Example from the specification
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(315));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
                controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
                controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(175));

                assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "Satellite2"),
                                controller.communicableEntitiesInRange("Satellite1"));
                assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"),
                                controller.communicableEntitiesInRange("Satellite2"));
                assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2"),
                                controller.communicableEntitiesInRange("DeviceB"));

                assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"),
                                controller.communicableEntitiesInRange("Satellite3"));
        }

        @Test
        public void testSomeExceptionsForSend() {
                // just some of them... you'll have to test the rest
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "Hey";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertThrows(FileTransferException.VirtualFileNotFoundException.class,
                                () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                controller.simulate(msg.length() * 2);
                assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class,
                                () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        }

        @Test
        public void testMovement() {
                // Task 2
                // Example from the specification
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(340));
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER,
                                "StandardSatellite"), controller.getInfo("Satellite1"));
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(337.95), 100 + RADIUS_OF_JUPITER,
                                "StandardSatellite"), controller.getInfo("Satellite1"));
        }

        @Test
        public void testExample() {
                // Task 2
                // Example from the specification
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "Hey";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

                controller.simulate(msg.length() * 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
                assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                                controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

                controller.simulate(msg.length());
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

                // Hints for further testing:
                // - What about checking about the progress of the message half way through?
                // - Device/s get out of range of satellite
                // ... and so on.
        }

        @Test
        public void testRelayMovement() {
                // Task 2
                // Example from the specification
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(180));

                // moves in negative direction
                assertEquals(
                                new EntityInfoResponse("Satellite1", Angle.fromDegrees(180), 100 + RADIUS_OF_JUPITER,
                                                "RelaySatellite"),
                                controller.getInfo("Satellite1"));
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(178.77), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(177.54), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(176.31), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));

                controller.simulate(5);
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(170.18), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                controller.simulate(24);
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                // edge case
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(139.49), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                // coming back
                controller.simulate(1);
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                controller.simulate(5);
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(146.85), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
        }

        @Test
        public void testTeleportingMovement() {
                // Test for expected teleportation movement behaviour
                BlackoutController controller = new BlackoutController();

                controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(0));

                controller.simulate();
                Angle clockwiseOnFirstMovement = controller.getInfo("Satellite1").getPosition();
                controller.simulate();
                Angle clockwiseOnSecondMovement = controller.getInfo("Satellite1").getPosition();
                assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

                // It should take 250 simulations to reach theta = 180.
                // Simulate until Satellite1 reaches theta=180
                controller.simulate(250);

                // Verify that Satellite1 is now at theta=0
                assertTrue(controller.getInfo("Satellite1").getPosition().toDegrees() % 360 == 0);
        }

        // MY TESTS:

        @Test
        public void testDeviceLeftRangeBeforeTransfer() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "StandardSatellite", 45000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "Hey hello how are you";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                controller.simulate(msg.length() * 2);
                assertNotEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        }

        @Test
        public void testSatelliteLeftRangeBeforeTransfer() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "Hey how are you";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                controller.simulate(msg.length() * 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                controller.createSatellite("Satellite2", "StandardSatellite", 159000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(292));
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "Satellite2"));
                controller.simulate(msg.length() * 2);
                assertNotEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite2").getFiles().get("FileAlpha"));
        }

        @Test
        public void sendingToRelay() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "RelaySatellite", 45000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "Hey hello how are you";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertThrows(FileTransferException.VirtualFileNoBandwidthException.class,
                                () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        }

        @Test
        public void sendingThroughRelays() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "StandardSatellite", 23000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(300));
                controller.createSatellite("Satellite2", "RelaySatellite", 20000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(190));
                controller.createSatellite("Satellite3", "RelaySatellite", 15000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(100));
                controller.createSatellite("Satellite4", "RelaySatellite", 34000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(240));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(160));

                String msg = "Hey how are you doing I am well thanks";
                controller.addFileToDevice("DeviceB", "FileAlpha", msg);

                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        }

        @Test
        public void testSendPartialFile() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "Hey how are you";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                controller.simulate(msg.length() * (1 / 2));
                assertNotEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                controller.createSatellite("Satellite2", "StandardSatellite", 12000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(300));
                assertThrows(FileTransferException.VirtualFileNotFoundException.class,
                                () -> controller.sendFile("FileAlpha", "Satellite1", "Satellite2"));
                controller.simulate(msg.length());
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "Satellite2"));
                controller.simulate(msg.length() * 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite2").getFiles().get("FileAlpha"));
        }

        @Test
        public void testSendAlreadySentFile() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "Hey how are you";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                controller.simulate(msg.length() * (1 / 2));
                assertNotEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class,
                                () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                controller.simulate(msg.length() * 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        }

        @Test
        public void testMaxFilesReached() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(190));
                controller.createSatellite("Satellite2", "RelaySatellite", 5000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(190));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(160));

                String msg = "Hey how are you";
                controller.addFileToDevice("DeviceB", "FileAlpha", msg);
                controller.addFileToDevice("DeviceB", "FileBeta", msg);
                controller.addFileToDevice("DeviceB", "FileGamma", msg);
                controller.addFileToDevice("DeviceB", "FileOver", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileBeta", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileBeta"));
                assertDoesNotThrow(() -> controller.sendFile("FileGamma", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileGamma", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileGamma"));
                assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class,
                                () -> controller.sendFile("FileOver", "DeviceB", "Satellite1"));
        }

        @Test
        public void testMaxStorageReached() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "StandardSatellite", 23000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(300));
                controller.createSatellite("Satellite2", "RelaySatellite", 20000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(190));
                controller.createSatellite("Satellite3", "RelaySatellite", 15000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(100));
                controller.createSatellite("Satellite4", "RelaySatellite", 34000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(240));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(160));

                String msg = "Hey how are you doing I am well thanks";
                controller.addFileToDevice("DeviceB", "FileAlpha", msg);
                controller.addFileToDevice("DeviceB", "FileBeta", msg);
                controller.addFileToDevice("DeviceB", "FileGamma", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileBeta", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileBeta"));
                assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class,
                                () -> controller.sendFile("FileGamma", "DeviceB", "Satellite1"));

        }

        @Test
        public void testTeleporterMaxStorageReached() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "TeleportingSatellite", 23000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(95));
                controller.createSatellite("Satellite2", "RelaySatellite", 20000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(160));
                controller.createSatellite("Satellite3", "RelaySatellite", 15000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(110));
                controller.createSatellite("Satellite4", "RelaySatellite", 34000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(95));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(160));

                String msg = "Hey how are you doing I am well thanks Hey how are you doing I am well thanks";
                controller.addFileToDevice("DeviceB", "FileAlpha", msg);
                controller.addFileToDevice("DeviceB", "FileBeta", msg);
                controller.addFileToDevice("DeviceB", "FileGamma", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileBeta", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileBeta"));
                assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class,
                                () -> controller.sendFile("FileGamma", "DeviceB", "Satellite1"));

        }

        @Test
        public void testMaxBandwidthReached() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(190));
                controller.createSatellite("Satellite2", "RelaySatellite", 5000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(190));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(160));

                String msg = "Hey how are you";
                controller.addFileToDevice("DeviceB", "FileAlpha", msg);
                controller.addFileToDevice("DeviceB", "FileBeta", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceB", "Satellite1"));
                assertThrows(FileTransferException.VirtualFileNoBandwidthException.class,
                                () -> controller.sendFile("FileBeta", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceB", "Satellite1"));
                controller.simulate(msg.length() + 2);
                assertEquals(new FileInfoResponse("FileBeta", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileBeta"));
        }

        @Test
        public void testPartialTransfer() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(190));
                controller.createSatellite("Satellite2", "RelaySatellite", 5000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(190));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(160));

                String msg = "Hey how are you";
                controller.addFileToDevice("DeviceB", "FileAlpha", msg);
                controller.addFileToDevice("DeviceB", "FileBeta", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceB", "Satellite1"));
                controller.simulate(4);
                assertEquals(4, controller.getInfo("Satellite1").getFiles().get("FileAlpha").getData().length());
        }

        @Test
        public void testTeleportingPartialTransfer() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(140));
                controller.createSatellite("Satellite2", "RelaySatellite", 5000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(160));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(160));

                String msg = "Hey how are you";
                controller.addFileToDevice("DeviceB", "FileAlpha", msg);
                controller.addFileToDevice("DeviceB", "FileBeta", (msg + msg));
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceB", "Satellite1"));
                controller.simulate(1);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceB", "Satellite1"));
                controller.simulate(1);
                assertEquals(15, controller.getInfo("Satellite1").getFiles().get("FileBeta").getData().length());
                controller.simulate(1);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        }
}
