#Runtime: 112 ms, faster than 88.79% of Python3 online submissions for Palindrome Number.
#Memory Usage: 13.3 MB, less than 5.03% of Python3 online submissions for Palindrome Number.

def isPalindrome(x):
    if x < 0:
        return False
    numList = []
    while (x > 0):
        numList.append(x % 10)
        x = x // 10
    for i in range(0,int(len(numList)/2)) :
        if numList[-(i+1)] != numList[i]:
            return False
    return True
def isPalindrome2(x):
    return str(x) == str(x)[::-1]