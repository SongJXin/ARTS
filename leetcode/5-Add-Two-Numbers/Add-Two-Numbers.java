//Runtime: 2 ms, faster than 96.54% of Java online submissions for Add Two Numbers.
//Memory Usage: 40.6 MB, less than 74.02% of Java online submissions for Add Two Numbers.
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
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
            lsum.next = addTwoNumbers(l1.next, l2.next);
        }
        else
        {
            lsum.next = null;
        }
        return lsum;
    }
}