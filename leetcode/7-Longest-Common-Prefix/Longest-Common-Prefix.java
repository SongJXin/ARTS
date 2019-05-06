//Runtime: 1 ms, faster than 89.54% of Java online submissions for Longest Common Prefix.
//Memory Usage: 38.9 MB, less than 18.52% of Java online submissions for Longest Common Prefix.

class Solution {
    public String longestCommonPrefix(String[] strs) {
        String result = "";
        if (strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        int min1 = strs[0].length();
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].length() < min1) {
                min1 = strs[i].length();
            }
        }
        for (int i = 0; i < min1; i++) {
            Boolean judge = true;
            char preChar = strs[0].charAt(i);
            for (int j = 0; j < strs.length; j++) {
                if (strs[j].charAt(i) != preChar) {
                    judge = false;
                    break;
                }
            }
            if (judge) {
                result = strs[0].substring(0, i + 1);
            } else {
                break;
            }
        }
        return result;
    }
}