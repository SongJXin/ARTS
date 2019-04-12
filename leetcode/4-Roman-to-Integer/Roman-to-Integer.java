//Runtime: 3 ms, faster than 100.00% of Java online submissions for Roman to Integer.
//Memory Usage: 37.8 MB, less than 33.25% of Java online submissions for Roman to Integer.
class Solution {
    public int romanToInt(String s) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'I') {
                if (i != s.length() - 1 && s.charAt(i + 1) == 'V') {
                    result += 4;
                    i++;
                } else if (i != s.length() - 1 && s.charAt(i + 1) == 'X') {
                    result += 9;
                    i++;
                } else {
                    result++;
                }
            } else if (s.charAt(i) == 'X') {
                if (i != s.length() - 1 && s.charAt(i + 1) == 'L') {
                    result += 40;
                    i++;
                } else if (i != s.length() - 1 && s.charAt(i + 1) == 'C') {
                    result += 90;
                    i++;
                } else {
                    result += 10;
                }
            } else if (s.charAt(i) == 'C') {
                if (i != s.length() - 1 && s.charAt(i + 1) == 'D') {
                    result += 400;
                    i++;
                } else if (i != s.length() - 1 && s.charAt(i + 1) == 'M') {
                    result += 900;
                    i++;
                } else {
                    result += 100;
                }
            } else if (s.charAt(i) == 'V') {
                result += 5;
            } else if (s.charAt(i) == 'L') {
                result += 50;
            } else if (s.charAt(i) == 'D') {
                result += 500;
            } else if (s.charAt(i) == 'M') {
                result += 1000;
            }
        }
        return result;
    }
}