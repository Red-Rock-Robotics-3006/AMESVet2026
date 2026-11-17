// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsytems.Hinge;
import frc.robot.subsytems.EndEffector;

public class RobotContainer {
  private static Hinge wrist = Hinge.getInstance();
  private static EndEffector endEffector = EndEffector.getInstance();

  private WristState CurrentWristState;

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
    configureBindings();
  }

  private void configureBindings() {
    // driveStick.leftTrigger(0.85)
    //     .onTrue(getAutonomousCommand())
    //     .onFalse(getAutonomousCommand());

    // driveStick.rightTrigger(0.85)
    //     .onTrue(wrist.outakeL1AngleCommand())
    //     .onFalse(getAutonomousCommand());
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
