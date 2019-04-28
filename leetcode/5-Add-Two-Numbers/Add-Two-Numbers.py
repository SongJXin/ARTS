#Runtime: 84 ms, faster than 85.57 % of Python3 online submissions for Add Two Numbers.
#Memory Usage: 13.4 MB, less than 5.21 % of Python3 online submissions for Add Two Numbers.

# Definition for singly-linked list.
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None


class Solution:
    def addTwoNumbers(self, l1: ListNode, l2: ListNode) -> ListNode:
        lsum = ListNode(0)
        carry = False
        if (l1.next == None and l2.next != None):
            lnew = ListNode(0)
            l1.next = lnew
        elif(l1.next != None and l2.next == None):
            lnew = ListNode(0)
            l2.next = lnew
        sum = 0
        sum = l1.val + l2.val
        if (sum >= 10):
            carry = True
            sum = sum % 10
        lsum.val = sum
        if(carry):
            if (l1.next != None):
                l1.next.val += 1
            else:
                lnew = ListNode(1)
                lsum.next = lnew
                return lsum
        if (l1.next != None):
            lsum.next = self.addTwoNumbers(l1.next, l2.next)
        else:
            lsum.next = None
        return lsum
