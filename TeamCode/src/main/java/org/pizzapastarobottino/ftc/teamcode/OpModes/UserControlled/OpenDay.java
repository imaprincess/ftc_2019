package org.pizzapastarobottino.ftc.teamcode.OpModes.UserControlled;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Consumer;
import org.pizzapastarobottino.ftc.teamcode.Configs;
import org.pizzapastarobottino.ftc.teamcode.Hardware.Hardware;
import org.pizzapastarobottino.ftc.teamcode.Hardware.Mechanism;
import org.pizzapastarobottino.ftc.teamcode.Hardware.UndeliverablePowerException;


/**
 * Created by Daniela on 01/06/18.
 */

@TeleOp(name = "OpenDay", group = "A")

public class OpenDay extends OpMode {
    private Hardware robot = new Hardware();
    
    double[] powers = new double[7];
    private final int ANTERIORE_DX = 0;
    private final int ANTERIORE_SX = 1;
    private final int POSTERIORE_DX = 2;
    private final int POSTERIORE_SX = 3;

    private final double ANT_SX_POWER = 0.1;
    private final double ANT_DX_POWER = 0.2;
    private final double POS_SX_POWER = 0.1;
    private final double POS_DX_POWER = 0.4;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    private void antipanico() {
        robot.forEachMechanism(new Consumer<Mechanism>() {
            @Override
            public void accept(Mechanism mechanism) {
                mechanism.stop();
            }
        });
    }

    private void tellState(){
        telemetry.addLine("Analogico sinistro -> X: " + gamepad2.left_stick_x + " Y:" + gamepad2.left_stick_y);
        telemetry.addLine("Analogico destro -> X: " + gamepad2.right_stick_x + " Y:" + gamepad2.right_stick_y);
        telemetry.addLine("Bottoni -> A: " + gamepad2.a + " B: " + gamepad2.b + " X: " + gamepad2.x + " Y: " + gamepad2.y);
        telemetry.addLine("Dpad -> Su: " + gamepad2.dpad_up + " giu: " + gamepad2.dpad_down + " sx: " + gamepad2.dpad_left + " dx: " + gamepad2.dpad_right);
        telemetry.addLine("Bumpers -> sx " + gamepad2.left_bumper + " dx: " + gamepad2.right_bumper);
        telemetry.addLine("Trigger -> sx: " + gamepad2.left_trigger +  " dx: " + gamepad2.right_trigger);

    }

    private void trasla(boolean up, boolean down, boolean l, boolean r) {

        if(up && down && l && r) return;

        if(up) {
            if(l) {
                powers[ANTERIORE_DX] = -ANT_DX_POWER;
                powers[POSTERIORE_SX] = POS_SX_POWER;
            } else if(r) {
                powers[ANTERIORE_SX] = ANT_SX_POWER;
                powers[POSTERIORE_DX] = -POS_DX_POWER;
            } else {
                powers[ANTERIORE_DX] = -ANT_DX_POWER;
                powers[POSTERIORE_SX] = POS_SX_POWER;
                powers[ANTERIORE_SX] = ANT_SX_POWER;
                powers[POSTERIORE_DX] = -POS_DX_POWER;
            }
        }

        else if(down) {
            if(r) {
                powers[ANTERIORE_DX] = ANT_DX_POWER;
                powers[POSTERIORE_SX] = -POS_SX_POWER;
            } else if(l) {
                powers[ANTERIORE_SX] = -ANT_SX_POWER;
                powers[POSTERIORE_DX] = POS_DX_POWER;
            } else {
                powers[ANTERIORE_DX] = ANT_DX_POWER;
                powers[POSTERIORE_SX] = -POS_SX_POWER;
                powers[ANTERIORE_SX] = -ANT_SX_POWER;
                powers[POSTERIORE_DX] = POS_DX_POWER;
            }
        } else if(r) {
            powers[ANTERIORE_DX] = ANT_DX_POWER;
            powers[POSTERIORE_SX] = -POS_SX_POWER;
            powers[ANTERIORE_SX] = ANT_SX_POWER;
            powers[POSTERIORE_DX] = -POS_DX_POWER;
        } else if(l) {
            powers[ANTERIORE_DX] = -ANT_DX_POWER;
            powers[POSTERIORE_SX] = POS_SX_POWER;
            powers[ANTERIORE_SX] = -ANT_SX_POWER;
            powers[POSTERIORE_DX] = POS_DX_POWER;
        }
    }

    private void trasla(double delta, double x, double y){
        double[] powers2;
        double temp = -x;
        x = -y;
        y = temp;

        try {
            powers2 = MechanumWheels.getPowerFast(delta,
                    Math.atan2(
                            y * Configs.yDirectionFactor,
                            x * Configs.xDirectionFactor
                    ));
            //telemetry.addLine(String.valueOf(powers2[0]));
            //telemetry.addLine(String.valueOf(powers2[1]));
            powers[ANTERIORE_DX] = powers[POSTERIORE_DX] = powers2[0];
            powers[ANTERIORE_SX] = powers[POSTERIORE_SX] = powers2[1];
        } catch (UndeliverablePowerException e) {
            telemetry.addLine(e.getMessage());
            telemetry.update();
        }
    }

    private void giraSuTeStesso(double delta){
        powers[ANTERIORE_SX] = powers[POSTERIORE_SX] = delta;
        powers[ANTERIORE_DX] = powers[POSTERIORE_DX] = delta;
    }

    private void resetPowers(){
        for(int i = ANTERIORE_DX; i <= POSTERIORE_SX; i++)
            powers[i] = 0;
    }

    private boolean robotFermo(){
        for(int i = ANTERIORE_DX; i <= POSTERIORE_SX; i++)
            if(powers[i] > 0)
                return false;
        return true;
    }

    @Override
    public void loop() {

        tellState();

        powers = new double[7];

        if (gamepad2.y) {
            antipanico();
            return;
        }

        if (Math.abs(gamepad2.right_stick_x) > 0.1) {
            giraSuTeStesso(gamepad2.right_stick_x);
        }


        trasla(gamepad2.dpad_up, gamepad2.dpad_down, gamepad2.dpad_left, gamepad2.dpad_right);

        aggiorna();

    }

    private void aggiorna() {
        robot.getMotor(Configs.motorRuotaAnterioreDX).move(powers[ANTERIORE_DX] * Configs.ruotaAnterioreDXrotationFactor);
        robot.getMotor(Configs.motorRuotaAnterioreSX).move(powers[ANTERIORE_SX] * Configs.ruotaAnterioreSXrotationFactor);
        robot.getMotor(Configs.motorRuotaPosterioreDX).move(powers[POSTERIORE_DX] * Configs.ruotaPosterioreDXrotationFactor);
        robot.getMotor(Configs.motorRuotaPosterioreSX).move(powers[POSTERIORE_SX] * Configs.ruotaPosterioreSXrotationFactor);
    }
}