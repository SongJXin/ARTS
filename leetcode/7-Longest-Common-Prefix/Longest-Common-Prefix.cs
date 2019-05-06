//Runtime: 100 ms, faster than 96.30% of C# online submissions for Longest Common Prefix.
//Memory Usage: 22.5 MB, less than 53.66% of C# online submissions for Longest Common Prefix.
public class Solution {
    public string LongestCommonPrefix(string[] strs) {
                    string result = "";
            if (strs.Length == 0)
            {
                return "";
            }
            if (strs.Length == 1)
            {
                return strs[0];
            }
            int min1 = strs[0].Length;
            for (int i = 0; i < strs.Length; i++)
            {
                if (strs[i].Length < min1)
                {
                    min1 = strs[i].Length;
                }
            }
            for (int i = 0; i < min1; i++)
            {
                bool judge = true;
                char preChar = strs[0][i];
                for (int j = 0; j < strs.Length; j++)
                {
                    if (strs[j][i] != preChar)
                    {
                        judge = false;
                        break;
                    }
                }
                if (judge)
                {
                    result = strs[0].Substring(0, i + 1);
                }
                else
                {
                    break;
                }
            }
            return result;
    }
}