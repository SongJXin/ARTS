//Runtime: 4 ms, faster than 95.51% of Java online submissions for Longest Substring Without Repeating Characters.
//Memory Usage: 39.1 MB, less than 23.26% of Java online submissions for Longest Substring Without Repeating Characters.
class Solution {
    public int lengthOfLongestSubstring(String s) {
        System.out.println(s);
        String subS= "";
        int leftIndex = 0;
        int max = 0;
        for (int i = 0; i < s.length(); i ++){
            int repeat = subS.indexOf(s.charAt(i));
            if (repeat != -1 ){
                leftIndex = leftIndex + repeat + 1;
            }
            subS = s.substring(leftIndex,i+1);
            max = subS.length() > max? subS.length():max;
        }
        return max;
        
    }
}