#Runtime: 52 ms, faster than 19.45% of Python3 online submissions for Valid Parentheses.
#Memory Usage: 13.4 MB, less than 5.22 % of Python3 online submissions for Valid Parentheses.

class Solution:
    def isValid(self, s: str) -> bool:
        if len(s) == 0:
            return True
        if len(s) % 2 == 1:
            return False
        if s[0] == ')' or s[0] == '}' or s[0] == ']':
            return False
        st = ""
        i = 0
        for sc in s:
            if s[i] == '(' or s[i] == '{' or s[i] == '[':
                st = st + s[i]
                i = i + 1
                continue
            if s[i] == ']' and st[len(st)-1] != '[':
                return False
            if s[i] == ')' and st[len(st)-1] != '(':
                return False
            if s[i] == '}' and st[len(st)-1] != '{':
                return False
            st = st[0:len(st)-1]
            i = i+1
        if len(st) != 0:
            return False
        return True
