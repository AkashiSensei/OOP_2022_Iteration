public class VMWindows extends VirtualMachine{
    public VMWindows() {
    }

    public static VMFactory getFactory() {
        return VMWindows::new;
    }


    @Override
    public String getSysType() {
        return "Windows";
    }
}
