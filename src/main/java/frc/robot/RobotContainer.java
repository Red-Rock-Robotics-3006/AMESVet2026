// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import frc.robot.subsytems.Hinge;
import frc.robot.subsytems.EndEffector;

public class RobotContainer {
  private Hinge Wrist = Hinge.getInstance();
  private EndEffector EndEffector = frc.robot.subsytems.EndEffector.getInstance(); // wierd name clash had to specify by using "frc.robot.subsytems.EndEffector"

  private XboxController driveStick = new XboxController(0);
  
  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
