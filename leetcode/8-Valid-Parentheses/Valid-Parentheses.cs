//Runtime: 76 ms, faster than 54.99% of C# online submissions for Valid Parentheses.
//Memory Usage: 20 MB, less than 39.25% of C# online submissions for Valid Parentheses.

public class Solution {
    public bool IsValid(string s) {
            Stack<Char> st = new Stack<char>();
            if (s.Length == 0)
            {
                return true;
            }
            if (s.Length%2 == 1)
            {
                return false;
            }
            if(s[0]=='}'|| s[0] == ']'|| s[0] == ')')
            {
                return false;
            }
            for (int i = 0; i < s.Length; i++)
            {
                if (s[i] == '[' || s[i] == '{' || s[i] == '(')
                {
                    st.Push(s[i]);
                }
                if (s[i] == ')' && st.Pop() != '(')
                {
                    return false;
                }
                if (s[i] == ']' && st.Pop() != '[')
                {
                    return false;
                }
                if (s[i] == '}' && st.Pop() != '{')
                {
                    return false;
                }
            }
            if (st.TryPop(out char a))
            {
                return false;
            }
            return true;
    }
}