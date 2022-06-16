package array;//Given the head of a singly linked list, reverse the list, and return the
//reversed list. 
//
// 
// Example 1: 
//
// 
//Input: head = [1,2,3,4,5]
//Output: [5,4,3,2,1]
// 
//
// Example 2: 
//
// 
//Input: head = [1,2]
//Output: [2,1]
// 
//
// Example 3: 
//
// 
//Input: head = []
//Output: []
// 
//
// 
// Constraints: 
//
// 
// The number of nodes in the list is the range [0, 5000]. 
// -5000 <= Node.val <= 5000 
// 
//
// 
// Follow up: A linked list can be reversed either iteratively or recursively. 
//Could you implement both? 
// Related Topics Linked List Recursion 👍 12277 👎 214


//leetcode submit region begin(Prohibit modification and deletion)

import javax.sound.midi.Soundbank;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public  ListNode reverseList(ListNode head) {

        //创建空 Next
    ListNode pre=new ListNode();
    //head
    ListNode cur=head;
    while (cur!=null){
        ListNode next=cur.next;

        cur.next=pre;
        pre=cur;
        cur=next;
    }
    return pre;
    }

    public ListNode reverse(ListNode nextNode,ListNode preNode){

        ListNode node=nextNode.next;

        if (node==null){
            System.out.println("end Node Val==="+nextNode.val);
            System.out.println("end Next Node ===="+nextNode.next);
            System.out.println("Pre Node val==="+preNode.val);
            System.out.println("pre  Next Node==="+preNode.next.val);

            return nextNode.next=preNode;
        }

        ListNode r=reverse(node,nextNode);

        r.next=preNode;


        System.out.println("******************");
        System.out.println("R val"+r.val);
        System.out.println("R Next Val"+r.next.val);
        System.out.println("******************");

        System.out.println("=====================");
        System.out.println("Pre Val"+preNode.val);
        System.out.println("Pre Next Val"+preNode.next.val);
        System.out.println("=====================");

      return r;
    }

    public static void main(String[] args) {

        ListNode node5=new ListNode(5,null);
        ListNode node4=new ListNode(4,node5);

        ListNode node3=new ListNode(3,node4);

        ListNode node2=new ListNode(2,node3);

        ListNode head=new ListNode(1,node2);

        Solution so=new Solution();
        System.out.println("Input ListNode ");
        ListNode reNos=so.reverseList(head);
//        System.out.println("Output reversNode Value");
//        so.printNodeList(head);



    }

    public void printNodeList(ListNode head){
        if (null==head){
            System.out.println("null");
        }
        System.out.println(head.val);
        printNode(head.next);

    }
    public void  printNode(ListNode node){
        if (node.next==null){

        }
        System.out.println(node.val);
        printNode(node.next);
    }
}
//leetcode submit region end(Prohibit modification and deletion)
