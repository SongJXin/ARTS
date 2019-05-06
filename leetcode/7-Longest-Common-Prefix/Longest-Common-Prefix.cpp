//Runtime: 12 ms, faster than 28.64% of C++ online submissions for Longest Common Prefix.
//Memory Usage: 8.9 MB, less than 98.72% of C++ online submissions for Longest Common Prefix.
class Solution {
public:
    string longestCommonPrefix(vector<string>& strs) {
        	string result = "";
	if (strs.size() == 0) {
		return "";
	}
	if (strs.size() == 1) {
		return strs[0];
	}
	int min1 = strs[0].length();
	for (int i = 0; i < strs.size(); i++) {
		if (strs[i].length() < min1) {
			min1 = strs[i].length();
		}
	}
	for (int i = 0; i < min1; i++) {
		bool judge = true;
		char preChar = strs[0][i];
		for (int j = 0; j < strs.size(); j++) {
			if (strs[j][i] != preChar) {
				judge = false;
				break;
			}
		}
		if (judge) {
			result = strs[0].substr(0, i + 1);
		}
		else{
			break;
		}
	}
	return result;
    }
};