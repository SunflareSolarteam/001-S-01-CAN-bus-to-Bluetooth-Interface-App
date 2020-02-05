package nl.sunflare.sunflare_sensorviewer.Classes;

import java.util.HashMap;

class DataProcessorUtils {

    public static String getSlsStatusMessage(int i) {
        HashMap<Integer, String> slsMessages = new HashMap<>();
        slsMessages.put(0, "Fail safe stop");
        slsMessages.put(1, "2 phase PWM");
        slsMessages.put(2, "Loadless fault");
        slsMessages.put(3, "Over speed fault");
        slsMessages.put(4, "Offset fault");
        slsMessages.put(5, "Zero speed fault");
        slsMessages.put(7, "Phase loss fault");
        slsMessages.put(9, "Output limited due to minimal voltage");
        slsMessages.put(10, "Output cut off due to minimal voltage");
        slsMessages.put(11, "Switched off due to under voltage");
        slsMessages.put(13, "Output limited due to maximal voltage");
        slsMessages.put(14, "Output cut off due to maximal voltage");
        slsMessages.put(15, "Switched off due to over voltage");
        slsMessages.put(21, "Output limited due to maximal temperature");
        slsMessages.put(22, "Output cut off due to maximal temperature");
        slsMessages.put(23, "Switched off due to over temperature");

        if (slsMessages.containsKey(i)) {
            return slsMessages.get(i);
        } else {
            return slsMessages.get(0);
        }
    }

    private static String reverseHex(String originalHex) {
        // TODO: Validation that the length is even
        int lengthInBytes = originalHex.length() / 2;
        char[] chars = new char[lengthInBytes * 2];
        for (int index = 0; index < lengthInBytes; index++) {
            int reversedIndex = lengthInBytes - 1 - index;
            chars[reversedIndex * 2] = originalHex.charAt(index * 2);
            chars[reversedIndex * 2 + 1] = originalHex.charAt(index * 2 + 1);
        }
        return new String(chars);
    }

    public static float convertHexToValue(String hex, int divisor, boolean reverse, boolean canGoNegative) throws NumberFormatException {
        float value;
        if (reverse) {
            hex = reverseHex(hex);
        }
        if (canGoNegative) {
            value = (short) Integer.parseInt(hex, 16);
        } else {
            value = Integer.parseInt(hex, 16);
        }
        return value / divisor;
    }

    public static float convertMpptHexToValue(String hex, boolean divide) {
        Long a = Long.parseLong(hex, 16);
        float b = Float.intBitsToFloat(Integer.reverseBytes(a.intValue()));
        if (divide) {
            b = b / 1000;
        }
        return b;
    }

    public static String[] splitMessageData(String hex) {
        String[] holder = new String[2];
        try {
            String id = hex.substring(0, 3);
            String data = hex.substring(3);
            holder[0] = id;
            holder[1] = data;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Out of bounds error while splitting message : " + hex);
            return null;
        }
        return holder;
    }


}
