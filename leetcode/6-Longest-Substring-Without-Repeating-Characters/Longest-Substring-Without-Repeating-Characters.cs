//Runtime: 88 ms, faster than 79.67% of C# online submissions for Longest Substring Without Repeating Characters.
//Memory Usage: 23.8 MB, less than 33.07% of C# online submissions for Longest Substring Without Repeating Characters.
public class Solution {
    public int LengthOfLongestSubstring(string s) {
            int max = 0;
            string subS = "";
            int leftIndex = 0;
            for (int i = 0; i < s.Length; i++)
            {
                int repeat = subS.IndexOf(s[i]);
                if (repeat != -1)
                {
                    leftIndex = leftIndex + repeat + 1;
                }
                subS = s.Substring(leftIndex, i + 1 - leftIndex);
                max = subS.Length > max ? subS.Length : max;
            }
            return max;
    }
}