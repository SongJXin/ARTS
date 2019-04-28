//Runtime: 20 ms, faster than 87.81% of C++ online submissions for Longest Substring Without Repeating Characters.
//Memory Usage: 13.9 MB, less than 98.44% of C++ online submissions for Longest Substring Without Repeating Characters.
class Solution {
public:
    int lengthOfLongestSubstring(string s) {
        	int max = 0;
	string subS = "";
	int leftIndex = 0;
	for (int i = 0; i < s.length(); i++) {
		int repeat = subS.find(s[i]);
		if (repeat != -1) {
			leftIndex = leftIndex + repeat + 1;
		}
		subS = s.substr(leftIndex, i + 1 - leftIndex);
		max = subS.length() > max? subS.length():max;
	}
	return max;
    }
};