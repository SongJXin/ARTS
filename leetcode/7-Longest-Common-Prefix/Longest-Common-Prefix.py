#Runtime: 52 ms, faster than 19.70 % of Python3 online submissions for Longest Common Prefix.
#Memory Usage: 13.4 MB, less than 5.10 % of Python3 online submissions for Longest Common Prefix.
class Solution:
    def longestCommonPrefix(self, strs: List[str]) -> str:
        result=""
        if len(strs)<=0:
            return ""
        if len(strs) == 1:
            return strs[0]
        min1 = len(strs[0])
        for str1 in strs:
            if len(str1) < min1:
                min1 = len(str1)
        for i in range(0,min1):
            judge = True
            preChar = strs[0][i]
            for str1 in strs:
                if str1[i]!=preChar:
                    judge = False
                    break
            if judge:
                result = strs[0][0:i+1]
            else:
                break
        return result
