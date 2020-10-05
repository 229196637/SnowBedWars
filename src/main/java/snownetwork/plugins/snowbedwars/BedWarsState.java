package snownetwork.plugins.snowbedwars;

public enum  BedWarsState {
    Waiting,WaitStart,Gaming,BedBoom,Ending,Teleport;
    private static BedWarsState bedWarsState;

    public static void setBedWarsState(BedWarsState bedWarsState) {
        BedWarsState.bedWarsState = bedWarsState;
    }

    public static boolean isBedWarsState(BedWarsState bedWarsState2) {
        return bedWarsState==bedWarsState2;
    }
}
