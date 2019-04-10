package InferenceGraphs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import InterchangeFormat.IFException;

public class DJ{
	public static void main(String[] args){
		try{
		    // Read the example file and output the probability of B
			InferenceGraph G = new InferenceGraph("./data/example.bif");
			Vector nodes = G.get_nodes();//获得inferenceGraph object G 中的list of nodes
			InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(0));
			System.out.println(1111);
			String []var = n.get_values();
			for (int i= 0;i<var.length;i++){
				System.out.println(var[i]);
			}
			
			System.out.println("The First Output:");
			System.out.println(n.get_name());//获得节点的名称
			n.get_Prob().print();//得到给定点的概率表，返回一个概率函数object		

			//Create string of variable-value pairs for probability table at node 0*/
			//为变量-值 对儿 创造了概率表
			String[][] s = new String[1][2];
			s[0][0] = "B";//表中变量的名字
			s[0][1] = "False";//变量的值

			//Compute probability with given variable-value pairs;
			//计算给定的变量-值 对儿的概率
            System.out.println("The Second Output:");
			System.out.println(n.get_function_value(s));//获取给定节点(给定其父母的节点)的概率表的值，并得到给定的变量配置

            //get_parents() works too;
			//获得子孙节点
			Vector children = n.get_children();
			//Tests if this enumeration contains more elements.
			//true if and only if this enumeration object contains at least one more element to provide; false otherwise.
			for (Enumeration e = children.elements(); e.hasMoreElements();) {
                InferenceGraphNode no = (InferenceGraphNode)(e.nextElement());
                //nextElement()the next element of this enumeration.
                System.out.println("The Third Output:");
				System.out.println("\t" + no.get_name());  
                //Get the probability table object for the node 
				no.get_Prob().print();
			}				
			
		}
		catch(IFException e){
			System.out.println("Formatting Incorrect " + e.toString());
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found " + e.toString());
		}
		catch(IOException e){
			System.out.println("File not found " + e.toString());
		}
	}
}
