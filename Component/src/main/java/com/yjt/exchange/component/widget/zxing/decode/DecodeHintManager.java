package com.hynet.heebit.components.widget.zxing.decode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.zxing.DecodeHintType;
import com.hynet.heebit.components.constant.Regex;
import com.hynet.heebit.components.utils.LogUtil;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

final class DecodeHintManager {

    private DecodeHintManager() {}

    private static Map<String, String> splitQuery(String query) {
        Map<String, String> map = new HashMap<>();
        int pos = 0;
        while (pos < query.length()) {
            if (query.charAt(pos) == '&') {
                // Skip consecutive ampersand separators.
                pos++;
                continue;
            }
            int amp = query.indexOf('&', pos);
            int equ = query.indexOf('=', pos);
            if (amp < 0) {
                // This is the last element in the query, no more ampersand elements.
                String name;
                String text;
                if (equ < 0) {
                    // No equal sign
                    name = query.substring(pos);
                    name = name.replace('+', ' '); // Preemptively decode +
                    name = Uri.decode(name);
                    text = "";
                } else {
                    // Split name and text.
                    name = query.substring(pos, equ);
                    name = name.replace('+', ' '); // Preemptively decode +
                    name = Uri.decode(name);
                    text = query.substring(equ + 1);
                    text = text.replace('+', ' '); // Preemptively decode +
                    text = Uri.decode(text);
                }
                if (!map.containsKey(name)) {
                    map.put(name, text);
                }
                break;
            }
            if (equ < 0 || equ > amp) {
                // No equal sign until the &: this is a simple parameter with no value.
                String name = query.substring(pos, amp);
                name = name.replace('+', ' '); // Preemptively decode +
                name = Uri.decode(name);
                if (!map.containsKey(name)) {
                    map.put(name, "");
                }
                pos = amp + 1;
                continue;
            }
            String name = query.substring(pos, equ);
            name = name.replace('+', ' '); // Preemptively decode +
            name = Uri.decode(name);
            String text = query.substring(equ + 1, amp);
            text = text.replace('+', ' '); // Preemptively decode +
            text = Uri.decode(text);
            if (!map.containsKey(name)) {
                map.put(name, text);
            }
            pos = amp + 1;
        }
        return map;
    }

    static Map<DecodeHintType, ?> parseDecodeHints(Uri inputUri) {
        String query = inputUri.getEncodedQuery();
        if (query == null || query.isEmpty()) {
            return null;
        }

        // Extract parameters
        Map<String, String> parameters = splitQuery(query);

        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);

        for (DecodeHintType hintType : DecodeHintType.values()) {

            if (hintType == DecodeHintType.CHARACTER_SET ||
                    hintType == DecodeHintType.NEED_RESULT_POINT_CALLBACK ||
                    hintType == DecodeHintType.POSSIBLE_FORMATS) {
                continue; // This hint is specified in another way
            }

            String parameterName = hintType.name();
            String parameterText = parameters.get(parameterName);
            if (parameterText == null) {
                continue;
            }
            if (hintType.getValueType().equals(Object.class)) {
                // This is an unspecified type of hint content. Use the value as is.
                // TODO: Can we make a different assumption on this?
                hints.put(hintType, parameterText);
                continue;
            }
            if (hintType.getValueType().equals(Void.class)) {
                // Void hints are just flags: use the constant specified by DecodeHintType
                hints.put(hintType, Boolean.TRUE);
                continue;
            }
            if (hintType.getValueType().equals(String.class)) {
                // A string hint: use the decoded value.
                hints.put(hintType, parameterText);
                continue;
            }
            if (hintType.getValueType().equals(Boolean.class)) {
                // A boolean hint: a few values for false, everything else is true.
                // An empty parameter is simply a flag-style parameter, assuming true
                if (parameterText.isEmpty()) {
                    hints.put(hintType, Boolean.TRUE);
                } else if ("0".equals(parameterText) ||
                        "false".equalsIgnoreCase(parameterText) ||
                        "no".equalsIgnoreCase(parameterText)) {
                    hints.put(hintType, Boolean.FALSE);
                } else {
                    hints.put(hintType, Boolean.TRUE);
                }

                continue;
            }
            if (hintType.getValueType().equals(int[].class)) {
                // An integer array. Used to specify valid lengths.
                // Strip a trailing comma as in Java style array initialisers.
                if (!parameterText.isEmpty() && parameterText.charAt(parameterText.length() - 1) == ',') {
                    parameterText = parameterText.substring(0, parameterText.length() - 1);
                }
                String[] values = Pattern.compile(Regex.COMMA.getRegext()).split(parameterText);
                int[] array = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    try {
                        array[i] = Integer.parseInt(values[i]);
                    } catch (NumberFormatException ignored) {
                         LogUtil.Companion.getInstance().print("Skipping array of integers hint " + hintType + " due to invalid numeric value: '" + values[i]);
                        array = null;
                        break;
                    }
                }
                if (array != null) {
                    hints.put(hintType, array);
                }
                continue;
            }
             LogUtil.Companion.getInstance().print("Unsupported hint type '" + hintType + "' of type " + hintType.getValueType());
        }
         LogUtil.Companion.getInstance().print("Hints from the URI: " + hints);
        return hints;
    }

    static Map<DecodeHintType, Object> parseDecodeHints(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null || extras.isEmpty()) {
            return null;
        }
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);

        for (DecodeHintType hintType : DecodeHintType.values()) {

            if (hintType == DecodeHintType.CHARACTER_SET ||
                    hintType == DecodeHintType.NEED_RESULT_POINT_CALLBACK ||
                    hintType == DecodeHintType.POSSIBLE_FORMATS) {
                continue; // This hint is specified in another way
            }

            String hintName = hintType.name();
            if (extras.containsKey(hintName)) {
                if (hintType.getValueType().equals(Void.class)) {
                    // Void hints are just flags: use the constant specified by the DecodeHintType
                    hints.put(hintType, Boolean.TRUE);
                } else {
                    Object hintData = extras.get(hintName);
                    if (hintType.getValueType().isInstance(hintData)) {
                        hints.put(hintType, hintData);
                    } else {
                         LogUtil.Companion.getInstance().print("Ignoring hint " + hintType + " because it is not assignable from " + hintData);
                    }
                }
            }
        }
         LogUtil.Companion.getInstance().print("Hints from the Intent: " + hints);
        return hints;
    }

}
