/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;

public class centerArbWithTx extends CommandBase {
  /**
   * Creates a new centerArbWithTx.
   */
  Limelight limes;
  DriveTrain trains;
  boolean txNavSame = false;
  int center = 0;
  boolean centered = false;
  public centerArbWithTx(Limelight lime, DriveTrain train, int centers) {
    trains = train;
    limes=lime;
    center = centers;
    // Use addRequirements() here to declare subsystem dependencies.
  }
  public int side(int center){
   if(trains.returnAngle()>0){
     return -center;
   }
  return center;
}
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
   center = side(center); 
  }
  public int keepArbSign(int center, double input){
    if(input>center){
      return 1;
    }
    return -1;
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double tx = 0;
    SmartDashboard.putBoolean("executing", true);
    SmartDashboard.putNumber("center", center);
    if(limes.hasTarget()){
      trains.setVelocity();
      double p = SmartDashboard.getNumber("p value", 0.0);
      double error1 = SmartDashboard.getNumber("level 1 error",0.0);
      double deadband = SmartDashboard.getNumber("deadband", 0.0);
      tx = limes.getTx();
      SmartDashboard.putNumber("error gyro", Math.abs(trains.returnAngle()+center));
      SmartDashboard.putNumber("error lime", Math.abs(tx-center));
      SmartDashboard.putBoolean("final", txNavSame);
      if(Math.abs(Math.abs(tx)-center)>error1){
        SmartDashboard.putNumber("turn", -keepArbSign(center,tx)*Math.max(deadband,p*Math.abs(tx-center)));
        SmartDashboard.putNumber("value", Math.abs(tx-center));
        trains.setTurn(-keepArbSign(center,tx)*Math.max(0.1,p*Math.abs(tx-center)));
      }
      else{
        centered = true;
      }
    }else{
      trains.setSpeed(0);
      trains.setTurn(0);
      trains.setVelocity();
    }  
    if(Math.abs(trains.returnAngle()-center)<1&&Math.abs(tx-center)<1){
      txNavSame = true;
    }  
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SmartDashboard.putBoolean("executing", false);
    trains.setSpeed(0);
    trains.setTurn(0);
    trains.setVelocity();
    txNavSame = false;
    centered = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(center!=0){
      return txNavSame; 
    }else{
      return centered; 
    }
  }
}
