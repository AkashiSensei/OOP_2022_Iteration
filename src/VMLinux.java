public class VMLinux extends VirtualMachine{
    public VMLinux() {
    }

    public static VMFactory getFactory() {
        return VMLinux::new;
    }

    @Override
    public String getSysType() {
        return "Linux";
    }
}
