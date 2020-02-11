/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;

public class ArcadeDrive extends CommandBase {
  RobotContainer robotContainers;
  DriveTrain trains;
  Limelight limes;
  public ArcadeDrive(RobotContainer cont, DriveTrain train, Limelight lime) {
    robotContainers = cont;
    trains = train;
    limes=lime;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    trains.setSpeed(-robotContainers.leftJoystickX());
    trains.setVelocity();    
    SmartDashboard.putNumber("Robot Angle", trains.returnAngle());
    SmartDashboard.putNumber("Target Angle", limes.getTx());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    trains. setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
