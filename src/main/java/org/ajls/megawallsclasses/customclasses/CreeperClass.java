package org.ajls.megawallsclasses.customclasses;

public class CreeperClass implements CustomClassI{
    @Override
    public String getClassName() {
        return "苦力怕";  //改成在configfile直接读取方便
    }
    @Override
    public String getClassSimpleName() {
        return "苦力怕";
    }
}
