#Runtime: 68 ms, faster than 93.66% of Python3 online submissions for Longest Substring Without Repeating Characters.
#Memory Usage: 13.4 MB, less than 5.05% of Python3 online submissions for Longest Substring Without Repeating Characters.
class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        max = 0
        subS = ""
        leftIndex = 0
        j = 1
        for i in s:
            repeat = subS.find(i)
            if repeat != -1 :
                leftIndex = leftIndex + repeat + 1
            subS = s[leftIndex:j]
            if len(subS) > max:
                max = len(subS)
            j = j + 1
        return max