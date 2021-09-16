package com.company;

public class Shear {

    private double beamHeight;
    private double beamWidth;
    private double beamEffectiveDepth;
    private double shearStrength;
    private double degreeOfReinforcement;
    private double characteristicStrengthOfConcrete;


    public Shear(double beamHeight, double beamWidth, double beamEffectiveDepth,
                 double shearStrength, double degreeOfReinforcement, double characteristicStrengthOfConcrete) {
        this.beamHeight = beamHeight;
        this.beamWidth = beamWidth;
        this.beamEffectiveDepth = beamEffectiveDepth;
        this.shearStrength = shearStrength;
        this.degreeOfReinforcement = degreeOfReinforcement;
        this.characteristicStrengthOfConcrete = characteristicStrengthOfConcrete;
    }

    public double getDesignStrengthOfConcrete() {
        return characteristicStrengthOfConcrete/1.4;
    }

    public double getSizeFactor() {
        double sizeFactor = 1 + Math.sqrt(200/(beamEffectiveDepth*1000));
        if (sizeFactor < 2) {
            return sizeFactor;
        }else {
            return 2;
        }
    }

    public double getVMinValue () {
        double vMinValue = 0.035*Math.sqrt(Math.pow(getSizeFactor(),3)*characteristicStrengthOfConcrete);
        return vMinValue;

    }

    public double getVValue() {
        double vValue = (0.18/1.4)*getSizeFactor()*Math.pow(degreeOfReinforcement*characteristicStrengthOfConcrete,0.333);
        if (vValue>getVMinValue()){
            return vValue;
        }else {
            return getVMinValue();
        }
    }

    public double getShearResistance() {
        return getVValue()*beamWidth*beamEffectiveDepth;
    }

    public void printShearCondition() {
        System.out.println("Ved = " + shearStrength + " kN");
        System.out.println("pl = " + degreeOfReinforcement + " %");
        System.out.println("h = " + beamHeight + " m");
        System.out.println("b = " + beamWidth + " m");
        System.out.println("d = " + beamEffectiveDepth + " m");
        System.out.println("fck = " + characteristicStrengthOfConcrete + " MPa");
        System.out.println("fcd = " + getDesignStrengthOfConcrete() + " MPa");
        System.out.println("k = " + getSizeFactor());
        System.out.println("vRd,c = " + getVValue() + " MPa");
        System.out.println("vmin = " + getVMinValue() + " MPa");
        System.out.println("VRd,c = " + getShearResistance() + " MPa");
        if ((shearStrength/1000)<getShearResistance()) {
            System.out.println("wystarczające jest zbrojenie minimalne");
        }else {
            System.out.println("Należy wyznaczyć zbrojenie na ścinanie");
            printCtgThetaValue();
        }
    }

    public double getStrengthReductionFactor (){
        return 0.6 * (1-characteristicStrengthOfConcrete / 250);
    }

    public double getInnerLeverArm(){
        return 0.9*beamEffectiveDepth;
    }

    public double getCForce(){
        return 1 * getStrengthReductionFactor() * getDesignStrengthOfConcrete() * beamWidth * getInnerLeverArm();
    }

    public double getOmegaFactor(){
        return (0.5 * getCForce()) / (shearStrength/1000);
    }

    public double getCtgTheta(){
        double ctgTheta = getOmegaFactor() + Math.sqrt(Math.pow(getOmegaFactor(),2)-1);
        if (shearStrength < (1000 * 0.4 * getCForce())){
            return 2;
        }else if (shearStrength > (1000 * 0.5 * getCForce())) {
            return 0;
        }else {
            return ctgTheta;
        }
    }

    public void printCtgThetaValue(){
        System.out.println("v = " + getStrengthReductionFactor());
        System.out.println("z = " + getInnerLeverArm() + " m");
        System.out.println("C = " + getCForce());
        System.out.println("omega = " + getOmegaFactor());
        if (getCtgTheta() == 0 ){
            System.out.println("Za mały przekrój betonowy");
        }else {
            System.out.println("ctgTheta = " + getCtgTheta());
        }
    }


}
