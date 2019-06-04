#Runtime: 44 ms, faster than 84.90 % of Python3 online submissions for Merge Two Sorted Lists.
#Memory Usage: 13.3 MB, less than 8.53 % of Python3 online submissions for Merge Two Sorted Lists.

class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None


class Solution:
    def mergeTwoLists(self, l1: ListNode, l2: ListNode) -> ListNode:
        if (l1 == None):
            return l2
        if (l2 == None):
            return l1
        if (l1.val > l2.val):
            l = l1
            l1 = l2
            l2 = l
        l = l1
        while (l2 != None):
            if l1.next == None:
                l1.next = l2
                break
            if l2.val <= l1.next.val:
                l3 = l1.next
                l1.next = l2
                l2 = l2.next
                l1.next.next = l3
            else:
                l1 = l1.next
        return l
