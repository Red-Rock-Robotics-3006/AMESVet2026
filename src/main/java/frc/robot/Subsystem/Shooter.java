package frc.robot.Subsystem;

//import static edu.wpi.first.units.Units.RPM;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import redrocklib.logging.SmartDashboardNumber;
import redrocklib.wrappers.RedRockTalon;

public class Shooter extends SubsystemBase{
    private static Shooter instance = null;


    private final RedRockTalon shooterMotor = new RedRockTalon(6767, "Shooter/shooterMotor"); //TODO

    private SmartDashboardNumber shooterSpeedL1 = new SmartDashboardNumber("Shooter/shooter-Speed-L1", 0.5);
    private SmartDashboardNumber shooterSpeedL2 = new SmartDashboardNumber("Shooter/shooter-Speed-L2", 0.5);
    private SmartDashboardNumber shooterSpeedL3 = new SmartDashboardNumber("Shooter/shooter-Speed-L3", 0.5);


    private SmartDashboardNumber reverseShooterSpeed = new SmartDashboardNumber("Shooter/reverse-shooter-Speed", 0.5);
    private SmartDashboardNumber reverseCurrentLimit = new SmartDashboardNumber("Shooter/reverse-Current-Limit", 10);


    private Shooter(){
        super("Shooter");

        this.shooterMotor.withMotorOutputConfigs(
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




    public void setShooterSpeed(double rpm){
        this.shooterMotor.motor.setControl(new VelocityVoltage(rpm/60).withSlot(0).withOverrideBrakeDurNeutral(true));
    }

    public Command stopShooterCommand(){
        return Commands.sequence(
            Commands.runOnce(() -> this.setShooterSpeed(0), this)
        );
    }

    public Command shootCommand1(){
        return Commands.sequence(
            Commands.runOnce(() -> this.setShooterSpeed(shooterSpeedL1.getNumber()), this)
        );
    }
    public Command shootCommand2(){
        return Commands.sequence(
            Commands.runOnce(() -> this.setShooterSpeed(shooterSpeedL2.getNumber()), this)
        );
    }
    public Command shootCommand3(){
        return Commands.sequence(
            Commands.runOnce(() -> this.setShooterSpeed(shooterSpeedL3.getNumber()), this)
        );
    }

    public Command reverseShooterCommand(){
        return new FunctionalCommand(
            () -> this.setShooterSpeed(reverseShooterSpeed.getNumber()), 
            () -> {}, 
            (interrupted) -> this.stopShooterCommand(), 
            () -> {
                return this.shooterMotor.motor.getSupplyCurrent().getValueAsDouble() > this.reverseCurrentLimit.getNumber();
            }, 
            this
        );
    }




    public static Shooter getInstance(){
        if(instance == null) instance = new Shooter();
        return instance;
    }
}





