// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// To veiw the controller bindings there is a PadCrafter link here:
// https://www.padcrafter.com/?col=%23242424%2C%23606A6E%2C%23FFFFFF&outline=5.5&templates=Wrist%2FArm%2FHinge+Controller+Scheme%7CDriver+Controller+Scheme&plat=0&timestamp=1794953988299&leftStick=%7CSwerve%3A+Translation&rightStick=&dpadDown=&rightStickClick=Stow+Wrist%7CSwerve%3A+Rotation&dpadLeft=Level+1&dpadRight=Level+2&dpadUp=Level+3&xButton=Intake+Shell+Pos&yButton=Intake+Cell+Pos&leftTrigger=&rightTrigger=%7CSpin+End+Effector%28Speed%2FDirection+Based+of+Wrist+Pos%29&aButton=Dock+Angle&bButton=Shelf+Angle

package frc.robot;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsytems.Hinge;
import frc.robot.subsytems.swerve.CommandSwerveDrivetrain;
import frc.robot.redrocklib.logging.SmartDashboardNumber;
import frc.robot.subsytems.Autos;
import frc.robot.subsytems.EndEffector;

public class RobotContainer {
  private static Hinge wrist = Hinge.getInstance();
  private static EndEffector endEffector = EndEffector.getInstance();
  private static Autos autonomous = Autos.getInstance();
  private static CommandSwerveDrivetrain csd = CommandSwerveDrivetrain.getInstance();
  private SendableChooser<Command> chooser = new SendableChooser<>();
  private final SwerveRequest.FieldCentric csdRequest = new SwerveRequest.FieldCentric().withDeadband(0.1);

  private SmartDashboardNumber maxTranslationProportion = new SmartDashboardNumber("Swerve/Max Translation Proportion", 0.7);
  private SmartDashboardNumber maxRotationProportion = new SmartDashboardNumber("Swerve/Max Rotation Proportion", 0.7);

  private WristState currentWristState;

  private enum WristState {
    STOW(wrist.stowAngleCommand(), endEffector.stopEndEffectorCommand()),
    DOCK(wrist.outakeShellAngleCommand(), endEffector.outakeL1Command()),
    SHELF(wrist.outakeNestedAngleCommand(), endEffector.outakeL1Command()),
    L1(wrist.outakeL1AngleCommand(), endEffector.outakeL1Command()),
    L2(wrist.outakeL2AngleCommand(), endEffector.outakeL2Command()),
    L3(wrist.outakeL3AngleCommand(), endEffector.outakeL3Command()),
    INTAKESHELL(wrist.intakeShellAngleCommand(), endEffector.intakeCommand()),
    INTAKECELL(wrist.intakeCellAngleCommand(), endEffector.intakeCommand());

    private Command wristPosCommand;
    private Command endEffectorCommand;

    WristState(Command wristPositionCommand, Command endEffectorSpeedCommand) {
      this.wristPosCommand = wristPositionCommand;
      this.endEffectorCommand = endEffectorSpeedCommand;
    }

    public Command getAngleSetCommand() {
      return this.wristPosCommand;
    }

    public Command getEndEffectorCommand() {
      return this.endEffectorCommand;
    }
  }

  private CommandXboxController driveStick = new CommandXboxController(0);
  private CommandXboxController wristStick = new CommandXboxController(0);

  public RobotContainer() {
    configureAutoChooser();
    configureBindings();
  }

  private void configureAutoChooser() {
    chooser.setDefaultOption("No Auto", Commands.runOnce(() -> System.out.print("NO AUTOS FOR YOU!")));
    chooser.addOption("Test", autonomous.test());
    chooser.addOption("Preloaded Doughnut", autonomous.preloadedDoughnut());
  }

  private void configureBindings() {
    ////#region -- Wrist Stick Bindings --
    wristStick.povLeft()
      .onTrue(WristState.L1.getAngleSetCommand());

    wristStick.povUp()
      .onTrue(WristState.L2.getAngleSetCommand());

    wristStick.povRight()
      .onTrue(WristState.L3.getAngleSetCommand());

    wristStick.a()
      .onTrue(WristState.DOCK.getAngleSetCommand());

    wristStick.b()
      .onTrue(WristState.SHELF.getAngleSetCommand());

    wristStick.x()
      .onTrue(WristState.INTAKESHELL.getAngleSetCommand());

    wristStick.y()
      .onTrue(WristState.INTAKECELL.getAngleSetCommand());

    wristStick.rightStick()
      .onTrue(WristState.STOW.getAngleSetCommand());//#endregion

    driveStick.rightTrigger(0.85)
      .onTrue(currentWristState.getEndEffectorCommand())
      .onFalse(endEffector.stopEndEffectorCommand());

    csd.setDefaultCommand(csd.applyRequest(() -> csdRequest.withVelocityX(-driveStick.getLeftY()*maxTranslationProportion.getNumber())
    													   .withVelocityY(-driveStick.getLeftX()*maxTranslationProportion.getNumber())
														   .withRotationalRate(-driveStick.getRightX()*maxRotationProportion.getNumber())));
  }

  public Command getAutonomousCommand() {
    return chooser.getSelected();
  }
}
