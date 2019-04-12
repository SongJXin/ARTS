#Runtime: 68 ms, faster than 92.31 % of Python3 online submissions for Roman to Integer.
#Memory Usage: 13.3 MB, less than 5.05 % of Python3 online submissions for Roman to Integer.
class Solution:
    def romanToInt(self, s: str) -> int:
        mapRoman = {"I":1,"V":5,"X":10,"L":50,"C":100,"D":500,"M":1000}
        result = 0
        i = 0
        while i < len(s):
            if s[i] == "I" and i != len(s)-1:
                if s[i+1] == "V":
                    result = result + 4
                    i = i + 1
                elif s[i+1] == "X":
                    result = result + 9
                    i = i + 1
                else:
                    result = result + 1
            elif s[i] == "X" and i != len(s)-1:
                if s[i+1] == "L":
                    result =  result + 40
                    i = i + 1
                elif s[i + 1] == "C":
                    result = result + 90
                    i = i + 1
                else :
                    result = result + 10
            elif s[i] == "C" and i != len(s)-1:
                if s[i+1]=="D":
                    result = result + 400
                    i = i + 1
                elif s[i+1]=="M":
                    result = result + 900
                    i = i + 1
                else:
                    result = result + 100
            else:
                result = result + mapRoman[s[i]]
            i = i + 1
        return result
