package org.pizzapastarobottino.ftc.teamcode;

import org.pizzapastarobottino.ftc.teamcode.Hardware.Hardware;

public class Movement {

    private final int ANTERIORE_DX = 0;
    private final int ANTERIORE_SX = 1;
    private final int POSTERIORE_DX = 2;
    private final int POSTERIORE_SX = 3;

    private final double ANT_SX_POWER = 0.1;
    private final double ANT_DX_POWER = 0.1;
    private final double POS_SX_POWER = 0.1;
    private final double POS_DX_POWER = 0.1;

    private double[] powers = new double[7];

    private Hardware robot;

    public Movement(Hardware robot) {
        this.robot = robot;
    }

    public void indietro(float potenza) {

        powers[ANTERIORE_DX] = -ANT_DX_POWER * potenza;
        powers[POSTERIORE_SX] = -POS_SX_POWER * potenza;
        powers[ANTERIORE_SX] = -ANT_SX_POWER * potenza;
        powers[POSTERIORE_DX] = -POS_DX_POWER * potenza;
    }

    public void avanti(float potenza) {

        powers[ANTERIORE_DX] = ANT_DX_POWER * potenza;
        powers[POSTERIORE_SX] = POS_SX_POWER * potenza;
        powers[ANTERIORE_SX] = ANT_SX_POWER * potenza;
        powers[POSTERIORE_DX] = POS_DX_POWER * potenza;
    }

    public void destra(float potenza) {

        powers[ANTERIORE_DX] = -ANT_DX_POWER * potenza;
        powers[POSTERIORE_SX] = -POS_SX_POWER * potenza;
        powers[ANTERIORE_SX] = ANT_SX_POWER * potenza;
        powers[POSTERIORE_DX] = POS_DX_POWER * potenza;
    }

    public void sinistra(float potenza) {

        powers[ANTERIORE_DX] = ANT_DX_POWER * potenza;
        powers[POSTERIORE_SX] = POS_SX_POWER * potenza;
        powers[ANTERIORE_SX] = -ANT_SX_POWER * potenza;
        powers[POSTERIORE_DX] = -POS_DX_POWER * potenza;
    }

    public void diagonaleSuSinistra(float potenza) {
        powers[ANTERIORE_DX] = ANT_DX_POWER * potenza;
        powers[POSTERIORE_SX] = POS_SX_POWER * potenza;
    }

    public void diagonaleSuDestra(float potenza) {
        powers[ANTERIORE_SX] = ANT_SX_POWER * potenza;
        powers[POSTERIORE_DX] = POS_DX_POWER * potenza;
    }

    public void diagonaleGiuSinistra(float potenza) {
        powers[ANTERIORE_SX] = -ANT_SX_POWER * potenza;
        powers[POSTERIORE_DX] = -POS_DX_POWER * potenza;
    }

    public void diagonaleGiuDestra(float potenza) {
        powers[ANTERIORE_DX] = -ANT_DX_POWER * potenza;
        powers[POSTERIORE_SX] = -POS_SX_POWER * potenza;
    }

    public void giraSuTeStesso(double delta){
        powers[ANTERIORE_SX] = powers[POSTERIORE_SX] = -delta;
        powers[ANTERIORE_DX] = powers[POSTERIORE_DX] = delta;
    }

    public void resetPowers(){
        for(int i = ANTERIORE_DX; i <= POSTERIORE_SX; i++)
            powers[i] = 0;
    }

    public boolean robotFermo(){
        for(int i = ANTERIORE_DX; i <= POSTERIORE_SX; i++)
            if(powers[i] > 0)
                return false;
        return true;
    }

    public void aggiorna() {

        robot.getMotor(Configs.motorRuotaAnterioreDX).move(powers[ANTERIORE_DX] * Configs.ruotaAnterioreDXrotationFactor);
        robot.getMotor(Configs.motorRuotaPosterioreDX).move(powers[POSTERIORE_DX] * Configs.ruotaPosterioreDXrotationFactor);
        robot.getMotor(Configs.motorRuotaAnterioreSX).move(powers[ANTERIORE_SX] * Configs.ruotaAnterioreSXrotationFactor);
        robot.getMotor(Configs.motorRuotaPosterioreSX).move(powers[POSTERIORE_SX] * Configs.ruotaPosterioreSXrotationFactor);
        powers = new double[7];
    }
}