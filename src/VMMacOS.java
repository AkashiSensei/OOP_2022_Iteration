public class VMMacOS extends VirtualMachine{
    public VMMacOS() {
    }

    public static VMFactory getFactory() {
        return VMMacOS::new;
    }

    @Override
    public String getSysType() {
        return "MacOS";
    }
}
