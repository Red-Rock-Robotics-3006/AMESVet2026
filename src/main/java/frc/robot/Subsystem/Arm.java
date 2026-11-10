package frc.robot.Subsystem;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import redrocklib.logging.SmartDashboardNumber;
import redrocklib.wrappers.RedRockTalon;

public class Arm extends SubsystemBase {
    private static Arm instance = null;

    private final RedRockTalon hingeMotor  = new RedRockTalon(5, "Arm/hingeMotor"); // TODO

    private SmartDashboardNumber output1Position = new SmartDashboardNumber("Arm/Arm-Output-1", 0.5);
    private SmartDashboardNumber output2Position = new SmartDashboardNumber("Arm/Arm-Output-2", 0.5);
    private SmartDashboardNumber output3Position = new SmartDashboardNumber("Arm/Arm-Output-3", 0.5);
    
    private SmartDashboardNumber inputCellPosition = new SmartDashboardNumber("Arm/Arm-Intake-Cell", 0.5);
    private SmartDashboardNumber inputShellPosition = new SmartDashboardNumber("Arm/Arm-Intake-Shell", 0.5);
    
    private SmartDashboardNumber shelfPosition = new SmartDashboardNumber("Arm/Arm-Shelf-Position", 0.5);

    private Arm(){
        super("Arm"); 

        this.hingeMotor.withMotorOutputConfigs(
            new MotorOutputConfigs()
            .withInverted(InvertedValue.CounterClockwise_Positive)
            .withPeakForwardDutyCycle(1d)
            .withPeakReverseDutyCycle(-1d)
            .withNeutralMode(NeutralModeValue.Coast)
        )
        .withSlot0Configs(
            new Slot0Configs()
            .withKA(0)
            .withKS(0)
            .withKV(0)
            .withKP(0)
            .withKI(0)
            .withKD(0)
        )
        .withMotionMagicConfigs(
            new MotionMagicConfigs()
            .withMotionMagicAcceleration(850)
            .withMotionMagicCruiseVelocity(150)
            .withMotionMagicJerk(10000000)
        )
        .withSpikeThreshold(17)
        .withCurrentLimitConfigs(
            new CurrentLimitsConfigs()
            .withSupplyCurrentLimit(45)
            .withSupplyCurrentLimitEnable(true)
            .withStatorCurrentLimit(60)
            .withStatorCurrentLimitEnable(true)
        ).withTuningEnabled(true);


    }


    public static Arm getInstance(){
        if(instance == null) instance = new Arm();
        return instance;
    }


    public void setHingePosition(double Position){
        hingeMotor.setMotionMagicPosition(Position);
    }

    public Command setPositionOutput1(){
        return Commands.runOnce(()-> this.setHingePosition(this.output1Position.getNumber()), this);
    }
    public Command setPositionOutput2(){
        return Commands.runOnce(()-> this.setHingePosition(this.output2Position.getNumber()), this);
    }
    public Command setPositionOutput3(){
        return Commands.runOnce(()-> this.setHingePosition(this.output3Position.getNumber()), this);
    }
    public Command setPositionInputCell(){
        return Commands.runOnce(()-> this.setHingePosition(this.inputCellPosition.getNumber()), this);
    }
    public Command setPositionInputShell(){
        return Commands.runOnce(()-> this.setHingePosition(this.inputShellPosition.getNumber()), this);
    }
    public Command setPositionShelf(){
        return Commands.runOnce(()-> this.setHingePosition(this.shelfPosition.getNumber()), this);
    }


}

