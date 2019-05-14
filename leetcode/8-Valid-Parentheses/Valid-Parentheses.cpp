//Runtime: 4 ms, faster than 97.76% of C++ online submissions for Valid Parentheses.
//Memory Usage: 8.6 MB, less than 99.75% of C++ online submissions for Valid Parentheses.

class Solution {
public:
    bool isValid(string s) {
    if (s.length() == 0) {
		return true;
	}
	if (s.length() % 2 == 1) {
		return false;
	}
	if (s[0] == ')' || s[0] == ']' || s[0] == '}') {
		return false;
	}
	stack<char> st ;
	for (int i = 0; i < s.length(); i++) {
		if (s[i] == '[' || s[i] == '{' || s[i] == '(') {
			st.push(s[i]);
			continue;
		}
		if (s[i] == ')' && st.top() != '(')
		{
			return false;
		}
		if (s[i] == ']' && st.top() != '[')
		{
			return false;
		}
		if (s[i] == '}' && st.top() != '{')
		{
			return false;
		}
		st.pop();
	}
	if (!st.empty()) {
		return false;
	}
	return true;
    }
};