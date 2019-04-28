//Runtime: 112 ms, faster than 66.10% of C# online submissions for Add Two Numbers.
//Memory Usage: 25.6 MB, less than 6.00% of C# online submissions for Add Two Numbers.

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     public int val;
 *     public ListNode next;
 *     public ListNode(int x) { val = x; }
 * }
 */
public class Solution {
    public ListNode AddTwoNumbers(ListNode l1, ListNode l2) {
                    ListNode lsum = new ListNode(0);
            Boolean carry = false;
            if(l1.next == null&& l2.next != null)
            {
                ListNode lnew = new ListNode(0);
                l1.next = lnew;
            }else if(l2.next == null && l1.next != null)
            {
                ListNode lnew = new ListNode(0);
                l2.next = lnew;
            }
            int sum = 0;
            sum = l1.val + l2.val;
            if (sum >= 10)
            {
                carry = true;
                sum = sum % 10;
            }
            lsum.val = sum;
            if (carry)
            {
                if(l1.next != null)
                {
                    l1.next.val += 1;
                }
                else
                {
                    ListNode lnew = new ListNode(1);
                    lsum.next = lnew;
                    return lsum;
                }
            }
            if (l1.next!= null)
            {
                lsum.next = AddTwoNumbers(l1.next, l2.next);
            }
            else
            {
                lsum.next = null;
            }
            return lsum;
    }
}