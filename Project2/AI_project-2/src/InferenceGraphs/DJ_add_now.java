package InferenceGraphs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import InterchangeFormat.IFException;

public class DJ_add_now {
	public static ArrayList record = new ArrayList();
	public static String[][] records= new String[11100][37];
	public static HashMap[] var = new HashMap[37];
	public static ArrayList<String> names=new ArrayList<String>();
	public static ArrayList<ArrayList<String>> re_data = new ArrayList<ArrayList<String>>();
	public static HashMap<String,Integer> maps = new HashMap<String,Integer>();//map为所有属性和index的映射表
	public static ArrayList<HashMap> combine = new ArrayList<HashMap>();//combine为所有父节点和自己的组合
	public static String[][] or_data= new String[11100][37];//存放样本数据
	public static ArrayList<ArrayList<Double>> theta=new ArrayList<ArrayList<Double>>();
	public static HashMap[] attribute_value_num = new HashMap[37];//每一行是一个map(属性对应的value T F：value的数目)
	public static ArrayList<ArrayList<Double>> pro= new ArrayList<ArrayList<Double>>();//存放父节点先验概率，每一行是一个属性，每一列对应每一个value的概率
	// Scan through the 1 dimensional data stored in the file
	static ArrayList<ArrayList<String>> re_value=new ArrayList<ArrayList<String>>();
	
	
	
	public static ArrayList<ArrayList<String>> readDataFromTheFile(String fileName){
		//ArrayList<ArrayList<String>>data = new ArrayList<ArrayList<String>>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 0;
			while((tempString = reader.readLine())!= null) {
				String [] temp =tempString.split("\\s++");
				ArrayList<String> list=new ArrayList(Arrays.asList(temp));
				list.add(0,Integer.toString(line));
				list.add(Double.toString(1.0));
				list.add("NoChange");
				list.add("P");
				line++;
				re_data.add(list);
			}
			for (int i =0;i<re_data.size();i++){
				for (int j=0;j<re_data.get(i).size();j++){
					String record=(String)re_data.get(i).get(j);
					//System.out.print(record);
				}
				//System.out.println("");
			}
			
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return re_data;
		
	}
	public static void read_data() //将样本存放到records里
	{
			String fileName = "./data/records.txt";
			File file = new File(fileName);
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				int line = 0;
				while((tempString = reader.readLine())!= null) {
					for (int i = 0; i<37; i++) {
						or_data[line][i] = tempString.split("\\s+")[i];
						or_data[line][i] = or_data[line][i].replace("\"","");
					}
					line++;
				}
			}catch(IOException e) 
			{
				e.printStackTrace();
			}
//		finally {
//				try {
//					for (int i = 0; i<11100; i++) {
//						System.out.print("line "+i);
//						for (int j = 0; j<37; j++) {
//							System.out.print(records[i][j]+" ");
//						}
//						System.out.println();
//					}
//					reader.close();
//				}catch(IOException e1) {
//				}
//			}	
		}

	
	public static void calculate_num() //计算每一列除去？的数目
	{
			try {
				read_data();
				InferenceGraph G = new InferenceGraph("./data/alarm.bif");
				Vector nodes = G.get_nodes();
				for (int i=0; i<37; i++) {
					InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
					HashMap<String, Integer> map = new HashMap<String, Integer>();
					String[] temp = n.get_values();//read records in lines
					for(int j=0; j<temp.length; j++) {
						map.put(temp[j], 0);
					}
					attribute_value_num[i] = map;
//					System.out.println(attribute_value_num[i]);
				}
				//统计每个属性的value的数目
				for(int i =0; i<11100; i++) {
					for(int j=0; j<37; j++) {
						String key = or_data[i][j];
//						System.out.println(key);
						if(key.equals("?"))
							continue;
						if(attribute_value_num[j].get(key) == null)
						{
							attribute_value_num[j].put(key, 1);
						}
						else 
						{
							int num = (int) attribute_value_num[j].get(key);
//							System.out.println(value);
							num++;
							attribute_value_num[j].put(key, num);
						}
					}
				}
				//输出map 类似{High=3201, Low=3378, Normal=4249, ?=272}
//				for(int i =0; i<37; i++)
//				{
//					System.out.println(attribute_value_num[i]);
//				}
			}catch(IFException e){
				System.out.println("Formatting Incorrect " + e.toString());
			}
			catch(FileNotFoundException e) {
				System.out.println("File not found " + e.toString());
			}
			catch(IOException e){
				System.out.println("File not found " + e.toString());
			}
		}
		
	public static void calculate_pro() 
		{
			try {
				calculate_num();
				InferenceGraph G = new InferenceGraph("./data/alarm.bif");
				Vector nodes = G.get_nodes();
				
				//计算每个属性的概率(pro[i])
				for (int i=0; i<37; i++) {
					InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
					//没有父节点
					if(n.get_parents().isEmpty()) 
					{
						ArrayList<Integer> num = new ArrayList<Integer>();//存放value的数目
						
						//用迭代器遍历
//						for(Iterator<Map.Entry<String, Integer>> it = attribute_value_num[i].entrySet().iterator(); it.hasNext();) 
//						{
//							System.out.print("11");
//							Map.Entry<String, Integer> entry = it.next();
//							Integer value = entry.getValue();
//							num.add(value);
//						}
						
						
				        //用for each遍历
						
						for (Object value:attribute_value_num[i].values()) 
						{
							num.add( (Integer) value);
						}
						int sum = 0;
						for(Integer integer : num) 
						{
							sum = sum + (int) integer;
						}	
						
						int j =0;
						ArrayList<Double> pro_row = new ArrayList<Double>();//存放一个属性的value概率
						for(Iterator it = num.iterator();it.hasNext();j++) 
						{
							int cur_num = (int) it.next();//当前value的数目
							double probability = (double)cur_num/sum;
							pro_row.add(( Double ) probability);
						}
						pro.add(pro_row);
						//输出先验概率
//						for(Double d : pro_row)
//						{
//							System.out.println(d);
//						}				
					 } 
					
					else //有父节点
					{
						//算有父节点的概率
					}
				}
				//输出先验概率  单纯测试
//				for(int i=0;i<pro.size();i++)
//				{
//					ArrayList<Double> pro_row = new ArrayList<Double>();
//					pro_row = pro.get(i);
//					for(int j=0;j<pro_row.size();j++)
//					{
//						System.out.print((double)pro_row.get(j)+" ");
//					}
//					System.out.println();
//				}
			}catch(IFException e){
				System.out.println("Formatting Incorrect " + e.toString());
			}
			catch(FileNotFoundException e) {
				System.out.println("File not found " + e.toString());
			}
			catch(IOException e){
				System.out.println("File not found " + e.toString());
			}
		}
	
	public static void get_name_A_value(){
		try {
			InferenceGraph G = new InferenceGraph("./data/alarm.bif");
			Vector nodes = G.get_nodes();
			for (int i=0; i<37; i++) {
				InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
				names.add(n.get_name());
				//System.out.println(names.get(i));//名字
				ArrayList<String> list=new ArrayList<String>();
				String[] temp = n.get_values();//read records in lines
				for(int j=0;j<temp.length;j++){
					list.add(temp[j]);
					//System.out.print(list.get(j));//值
				}
				re_value.add(list);
				//System.out.println("");
				
			}		
			
		}catch(IFException e){
			System.out.println("Formatting Incorrect " + e.toString());
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found " + e.toString());
		}
		catch(IOException e){
			System.out.println("File not found " + e.toString());
		}
	}
	public static void extend_records(){
		try {
			InferenceGraph G = new InferenceGraph("./data/alarm.bif");
			Vector nodes = G.get_nodes();
			int size=re_data.size();
			/*for(int m=0;m<re_data.size();m++){
				for (int n=0;n<re_data.get(m).size();n++){
					System.out.print(re_data.get(m).get(n)+" ");
				}
				System.out.println("");
			}*/
			ArrayList<String> row=new ArrayList<String>();//保存第几行的index
			for(int i=0;i<size;i++){//11100
				ArrayList<ArrayList<String>> list_temp0 = new ArrayList<ArrayList<String>>();//存储每行增加的记录
				ArrayList<String> listi=new ArrayList<String>();//获取到第i行
				listi = re_data.get(i);
				ArrayList<String> value=new ArrayList<String>();//存储有问号那个元素对应的值
				int len=0;//存储每行要增加多少记录
				int t=0;//知道要换第几个元素
				
				for(int j=0;j<40;j++){
					String value_ij=re_data.get(i).get(j).toString();
					if(value_ij.compareTo("\"?\"")==0){
						value = re_value.get(j-1);
						len=value.size();
						t=j;
						row.add(Integer.toString(i));
						//re_data.remove(row);
					}
				}
				/*if(row!=0){
					re_data.remove(row);
				}*/
				String listij=listi.get(t);//获取到第ij的元素
				for (int k=0;k<len;k++){
					ArrayList<String> temp=new ArrayList<String>();//存储每行元素
					temp=listi;
					//System.out.println("111"+listij+"***"+re_value.get(t-1).get(k));
					temp.set(t,"\""+re_value.get(t-1).get(k)+"\"");
					temp.set(39,";"+Integer.toString(t));
					list_temp0.add(k,(ArrayList<String>)temp.clone());//把要添加的行先添加到一个大的Arraylist里面
					
				}
				re_data.addAll(list_temp0);//合并两个大的
				
			}
			//re_data.remove(0);
				for(int j=0;j<row.size();j++){
					re_data.remove(Integer.parseInt(row.get(j))-j);
				}
				
				/*for(int i=0;i<re_data.size();i++){
					for(int j=0;j<re_data.get(0).size();j++){
						System.out.print(re_data.get(i).get(j));
					}
					System.out.println("");
				}*/
			System.out.println(re_data.size());
		}catch(IFException e){
			System.out.println("Formatting Incorrect " + e.toString());
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found " + e.toString());
		}
		catch(IOException e){
			System.out.println("File not found " + e.toString());
		}
	}
	
	public static void combine() {//记录父节点和自己的所有组合
		try {
			InferenceGraph G = new InferenceGraph("./data/alarm.bif");
			Vector nodes = G.get_nodes();
			for (int i=0; i<37; i++) 
			{
				InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
				String name = n.get_name();
				maps.put(name, (Integer)i);//给每个属性映射index
			}
			for(int i=0; i<37; i++)
			{
				InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
				HashMap<ArrayList,ArrayList> map = new HashMap<ArrayList,ArrayList>();//每个节点存放在一个map(父节点index：父节点组合)
				if(n.get_parents().isEmpty())//没有父节点直接存自己的组合
				{
					String[] values = n.get_values();
					ArrayList value = new ArrayList();
					for(int k=0;k<values.length;k++)
					{
						value.add(values[k]);
//						System.out.println(values[k]);
					}
					ArrayList key = new ArrayList();
					key.add(Integer.toString(i));
					map.put(key, value);
				}
				else //有父节点，遍历父节点
				{
					Vector parents = n.get_parents();
					ArrayList<String[]> values = new ArrayList<String[]>();//存放该节点的父节点的所有values
					ArrayList<Integer> par_index = new ArrayList<Integer>();//存放父节点INDEX组合
					ArrayList<String> value = new ArrayList<String>();//存放父节点的所有组合(字符串)情况
					for(Enumeration e = parents.elements(); e.hasMoreElements();)
					{
						InferenceGraphNode no = (InferenceGraphNode)(e.nextElement());
						String[] s=no.get_values();//每个父节点的value
						values.add(s);
						par_index.add(maps.get(no.get_name())) ;
					}
					par_index.add(maps.get(n.get_name()));
					//加上自己的value
					String[] self_values = n.get_values();
					values.add(self_values);
					test(value,values,values.get(0),"");//存放父亲节点values，第n个父亲节点value
					map.put(par_index, value);
				}
				combine.add(map);
				
			}
			/*for(int k=0;k<combine.size();k++)
			{
				System.out.println(combine.get(k));
			}*/
			
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

	public static void test( ArrayList<String> value,ArrayList<String[]> list ,String[]arr,String str)
	{
		for(int i=0;i<list.size();i++)
		{
			if (i==list.indexOf(arr))
			{
				for(String st:arr)
				{
					st=str+","+st;
					if(i<list.size()-1)
					{
						test(value,list, list.get(i+1),st);
					}
					else if(i==list.size()-1)
					{
						
						value.add(st);
//						System.out.println(st);
					}
				}
			}
		}
	}
	
	
	public static double calculate_probabilityij(int line,int row){//line行，row列
		//获得有问号节点的index
		/*for(int i=0;i<re_data.size();i++){
			int num1=Integer.parseInt(re_data.get(i).get(39).substring(1, re_data.get(i).get(39).length()))-1;
		}*/
		double pro=0.0;
		try {
			//获得了有问号的index
			//int num1=Integer.parseInt(re_data.get(line).get(38).substring(1, re_data.get(line).get(38).length()))-1;
			row=row-1;//row is j,line is i
			InferenceGraph G = new InferenceGraph("./data/alarm.bif");
			Vector nodes = G.get_nodes();
			//int weight=0;
			//InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(num));
			//找出含有n的子节点的组合
			//找出含有n=value[i]的组合
			
			//算P(x1,...,x37)的全概率
			//首先遍历每个属性，对应找父节点，父节点的组合去theta表中找概率
			/*for(int i=0;i<37;i++){*/
			ArrayList <String>combine_value=new ArrayList<String>();
			ArrayList /*<String>*/combine_key=new ArrayList/*<String>*/();
			//if(!((InferenceGraphNode)nodes.elementAt(row)).get_parents().isEmpty()){
				//有父节点》根据对应的子节点的index，去找作为子节点的父节点的组合》
				HashMap <ArrayList,ArrayList> map = new HashMap<ArrayList,ArrayList>();
				map=combine.get(row);//就是每个子节点对应的一个map
				//ArrayList<String> key_index=new ArrayList<String>();
				
				//int child_index =Integer.parseInt(key_index.get(key_index.size()-1));
				//child_index子节点的index就是ArrayList中的index
				//根据index找到对应的父节点和他自己的组合
				//然后去theta表中找到对应的概率
				
				for (Map.Entry entry : map.entrySet()) {  
			        Object key = entry.getKey();//key是一个list 
			        Object value = entry.getValue();//value也是一个list
			        combine_key.addAll((ArrayList)key);
			        combine_value.addAll((ArrayList<String>)value);
				}
				ArrayList<ArrayList<String>> value_new=convert_map_value_to_arr(combine_value);
				
				//System.out.println(value_new);
				//找记录中对应index的取值,index是combine_key的值
				ArrayList <String> record_i_value=new ArrayList<String>();
				
				for(int m=0;m<combine_key.size();m++){//index的个数
					//System.out.println("num="+(Integer.parseInt(combine_key.get(m).toString())+1));
					int num=Integer.parseInt(combine_key.get(m).toString())+1;
					//System.out.println(num);
					record_i_value.add((re_data.get(line).get(num).substring(1, re_data.get(line).get(num).length()-1)));
					//System.out.println(re_data.get(line).get(num));
					
				}
				//System.out.println("value_new"+value_new);
				//System.out.println("record_i_value"+record_i_value);
				//去匹配，得到index
				int index=0;
				for(int n=0;n<value_new.size();n++){
					//System.out.println(value_new.get(n)+"="+record_i_value);
					if((value_new.get(n)).equals(record_i_value)){
						index=n;
						//System.out.println("n="+n);
					}
				}
				//根据index去找theta中第row个节点对应的Arraylist的index对应的概率
				pro=(double)theta.get(row).get(index);
				//System.out.println("pro="+pro);
			/*}else{
				
			}*/
				
			/*}*/
		}catch(IFException e){
			System.out.println("Formatting Incorrect " + e.toString());
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found " + e.toString());
		}
		catch(IOException e){
			System.out.println("File not found " + e.toString());
		}
		return pro;
	}
	public static double calculate_TotalProbability_i(int i){
		double probability=1.0;
		for(int j=1;j<38;j++){
			probability *= calculate_probabilityij(i, j);
		}
		return probability;
	}
	public static void create_theta()
	{
		try
		{
			InferenceGraph G = new InferenceGraph("./data/alarm.bif");
			Vector nodes = G.get_nodes();
			int k=0;//表示第几个没有父节点的节点  来对应pro 因为pro只存了没有父节点的先验概率
			for(int i=0;i<37;i++){
				InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
				ArrayList<Double> row=new ArrayList<Double>();
				HashMap <ArrayList,ArrayList> map = new HashMap<ArrayList,ArrayList>();
				map=combine.get(i);//就是每个子节点对应的一个map
				//得到map中的value(ArrayList)的个数就是对应的theta表中概率的个数，对应的index就是对应的theta表中index的概率
				//取出map中value的大小
				ArrayList combine_value=new ArrayList();
				for (Map.Entry entry : map.entrySet()) {  
			        Object value = entry.getValue();//key是一个list 
			        combine_value.addAll((ArrayList)value);
				}
				//System.out.println("combine_value="+combine_value);
				int num=combine_value.size();//一共有num个组合
//				System.out.println("num="+num);
				
				//把对应的概率值加入到初始的theta表中
				if(n.get_parents().isEmpty())
				{
					for (int j=0;j<num;j++)
					{
						row.add(pro.get(k).get(j));//赋予对应的概率值
					}
					k++;
				}
				else //有父节点的节点的概率直接均分
				{
					for (int j=0;j<num;j++)
					{
						int size=re_value.get(i).size();
						double proba = (double) 1/size;
						Double have_parent_pro = (Double) proba;
						row.add(have_parent_pro);
					}
				}
				theta.add(row);
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
	public static void calculate_weight(){
		System.out.println("----------------calculate_weight---------------begin");
			//for(int i=0;i<re_data.size();i++){
		int i=0;
		while(i<re_data.size()){//遍历每一行
			int lines=0;//有一个行号相同就是几
			re_data.get(i).get(0);//每一行的行号
			//首先找到有几个行号相同的
			ArrayList <Double>list=new ArrayList<Double>();//存放每一个配对的全概率
			ArrayList <Double>list_all=new ArrayList<Double>();
			for(int j=i;j<re_data.size();j++){//j从i开始找
				/*//获得了有问号的index
				String index_wenh=re_data.get(i).get(39);
				int num1=Integer.parseInt(index_wenh.substring(1, index_wenh.length()));*/
				//找到第i行有几个配对的行
				if(re_data.get(i).get(0).equals(re_data.get(j).get(0))){
					lines=lines+1;
					double pro_i=calculate_TotalProbability_i(j);//每行的全概率
					list.add(pro_i);
				}
			}
			//System.out.println("list_配对的全概率"+list);
			double down=0.0;//一个配对组合的全概率之和
			for(int m=0;m<list.size();m++){
				down+=list.get(m);
			}
			for(int n=0;n<list.size();n++){
				list_all.add(list.get(n)/down);//每一行的权重
			}
			
			for(int k=0;k<lines;k++){
				re_data.get(i+k).set(38,list_all.get(k).toString());
				re_data.get(i+k).set(40,list.get(k).toString());
			}
			i=i+lines;
			//System.out.println("list_all_配对的权重"+list_all);
			//System.out.println(i);
		}
		/*for(int h=0;h<re_data.size();h++){
			System.out.print("wei="+re_data.get(h).get(38)+"//");
		}*/
		System.out.println("----------------calculate_weight----------------end");
	}
	public static void calculate_theta()
	{
		
		System.out.println("----------------calculate_theta----------------begin");
		
		double constant_weight=0.0;
		for(int i=0;i<re_data.size();i++){
			double num=Double.parseDouble(re_data.get(i).get(38));
			constant_weight += num;
		}
		//System.out.println("constan_weight="+constant_weight);
		for(int i=0; i<37; i++)
		{
			HashMap <ArrayList,ArrayList> map = new HashMap<ArrayList,ArrayList>();
			map=combine.get(i);//每个节点的父节点组合的map
			ArrayList combine_key=new ArrayList();
			ArrayList <String>combine_values=new ArrayList<String>();
			for (Map.Entry entry : map.entrySet())
			{  
		        Object key = entry.getKey(); //父节点的组合index list
		        Object values = entry.getValue(); //父节点的组合value list
		        combine_key.addAll((ArrayList)key);
		        combine_values.addAll((ArrayList<String>)values);
			}
			//System.out.println(i+"----------------------------begin");
			//System.out.println("combine_key="+combine_key);
			
			ArrayList<ArrayList<String>> value_new=convert_map_value_to_arr(combine_values);
			//System.out.println("combine_values="+value_new);
			
			int num = combine_key.size();
			int num2 = value_new.size();//有几个父节点的组合  比如TTF TTT 有几个
			//System.out.println("keynum="+num);
			//System.out.println("value_new_num2="+num2);
			ArrayList<ArrayList<Double>> combine_weights=new ArrayList<ArrayList<Double>>();//存放满足父节点组合的所有权重
//			double weights =0;
			
			for(int k= 0;k<num2;k++)
			{//遍历每一个子节点的每一种与父节点的组合
				ArrayList <String>values= value_new.get(k);//每一个  都  是一个arrylsit
				//ArrayList list = new ArrayList();//存放满足 index的对应列的值
				ArrayList<Double> combine_weight=new ArrayList<Double>();//存放算theta的分子上的权重   类似TTT
				ArrayList<Double> combine_par_weight=new ArrayList<Double>();//存放算theta的分母上的权重   类似TT
				//父节点和自己的权重
				for(int line=0;line<re_data.size();line++)//每一行
				{
					ArrayList list = new ArrayList();//每一行new一个，存放满足 index的对应列的值
					for(int j=0; j<num; j++ )
					{
						String index = combine_key.get(j).toString();
						//System.out.println("index="+index);
						int id=Integer.parseInt(index)+1;
						int len=re_data.get(line).get(id).length();
						list.add(re_data.get(line).get(id).substring(1,len -1));
					}
					if(values.equals(list)) {
						combine_weight.add(Double.parseDouble(re_data.get(line).get(38)));
					}
				}
				//去掉子节点的父节点
				
				double par_weight = 0.0;
				for(int line=0;line<re_data.size();line++)
				{	ArrayList copy_val=new ArrayList();
					copy_val.addAll(values);//copy values
					//System.out.println("2num="+num);
					//System.out.println("2num2="+num2);
					//ArrayList list = new ArrayList();//存放满足 index的对应列的值
					ArrayList list1 = new ArrayList();//存放满足 index的对应列的值
					int list1_size=list1.size();
					for(int j=0; j<num; j++ )
					{
						String index = combine_key.get(j).toString();
						//System.out.println("index="+index);
						int id=Integer.parseInt(index)+1;
						int len=re_data.get(line).get(id).length();
						list1.add(re_data.get(line).get(id).substring(1,len -1));
					}
					//System.out.println("list1="+list1);
					if(num>1) {
						//System.out.println("2222222222222");
//						System.out.println("******1*******");
//						System.out.println("1list="+list1);
//						System.out.println("1values="+values);
//						System.out.println("list.size()="+list1.size());
//						System.out.println("values.size()="+values.size());
						list1.remove(list1.size()-1);//去掉自己的父节点组合
						copy_val.remove(copy_val.size()-1);
//						System.out.println("2list="+list1);
//						System.out.println("2values="+copy_val);
//						System.out.println("******2*******");
					}
					//System.out.println(list1.equals(copy_val));
					if (list1.equals(copy_val))
					{
						if(num>1){
							//System.out.println("22222222222222"+Double.parseDouble(re_data.get(line).get(38)));
							combine_par_weight.add(Double.parseDouble(re_data.get(line).get(38)));
						}else{
							par_weight = constant_weight;
						}
						//System.out.println("111111111111parenrt_weight"+combine_par_weight);
					}
					
				}
				
				double weight = 0.0 ;
				for(int j=0; j<combine_weight.size(); j++ )
				{
					weight+=combine_weight.get(j);
				}
//				weights+=weight;
				System.out.println("weight="+combine_weight);
				//double par_weight = 0;
				for(int j =0; j<combine_par_weight.size(); j++)
				{
					par_weight+=combine_par_weight.get(j);
				}
				System.out.println("par_weight="+par_weight);
				if(par_weight>0){
				theta.get(i).set(k, weight/par_weight);
				}else{
					int n=re_value.get(i).size();
					theta.get(i).set(k,(double)1.0/n);
				}
//				combine_weights.add(combine_weight);
				
			}
			
//			for(int k= 0;k<num2;k++)
//			{
//				double weight = 0;
//				ArrayList combine_weight = combine_weights.get(k);
//				for (int m=0; m<combine_weight.size();m++)
//				{
//					weight+=(double)combine_weight.get(m);
//				}
//				theta.get(i).set(k, weight/weights);
//			}
		}
	
		System.out.println("----------------calculate_theta----------------end");
	}
	
	public static double calculateLogLikelyHood(){
		double result=0.0;
		for(int i=0;i<re_data.size();i++){
			double sum=0.0;
			sum=(Double.parseDouble(re_data.get(i).get(38).toString()))*(Double.parseDouble(re_data.get(i).get(40).toString()));
			result+=Math.log(sum);
		}
		return result;
	}
	public static ArrayList convert_map_value_to_arr(ArrayList<String>list){
		ArrayList<ArrayList> value_list=new ArrayList<ArrayList> ();
			for (int i=0;i<list.size();i++){
				if(list.get(i).toString().trim().startsWith(",")) {
					String value = list.get(i).toString().trim().substring(1, list.get(i).length());//获得去掉i的String
					ArrayList<String> value_arr=new ArrayList<String>(Arrays.asList(value.split(",")));
					value_list.add(value_arr);
				}else {
					String value1 = list.get(i).toString().trim();
					ArrayList<String> value_arr=new ArrayList<String>();
					value_arr.add(value1);		
					value_list.add(value_arr);
				}
			
		}
		return value_list;
	}
	
	public static boolean converged(double logliklyhood, double previousLogLiklyHood) {
		double diff = 0.000001;
		if (logliklyhood - previousLogLiklyHood < diff) {
			return true;
		}
		return false;
	}
	
	public static void calculate_con_weight() {
		for(int i=0;i<re_data.size();i++) {
			
			int size=Integer.parseInt(re_data.get(i).get(39).substring(1,re_data.get(i).get(39).length()));//得到他的行号
			double wei=0.0;
			double weight=1.0/re_value.get(size-1).size();
			re_data.get(i).set(38,Double.toString(weight));
		}System.out.println("records");
		for (int i=0;i<100;i++) {
			System.out.println(re_data.get(i));
		}
	}
	
	
	public static void main(String[] args) {
		try {
			InferenceGraph G = new InferenceGraph("./data/alarm.bif");
			Vector nodes = G.get_nodes();
			String filename = "./data/records.txt";
			readDataFromTheFile(filename);
			get_name_A_value();
			extend_records();
			calculate_pro();
			combine();
			create_theta();
			//double t=calculate_probabilityij(1,5);
			//System.out.println(t);
			//System.out.println("*****************************************");
			//System.out.println("*****************************************");
			
			double prevLogLikeliHood = 0;//极大似然
			double logLikeliHood = 0;//对数似然
			int Step=0;
			do{
				System.out.println("Step="+Step+"begin");
				calculate_weight();
				for(int i=0;i<re_data.size();i++){
					System.out.print("index="+i+"weight="+re_data.get(i).get(38)+"*****P="+re_data.get(i).get(38)+"////");
				}
				prevLogLikeliHood=calculateLogLikelyHood();
				calculate_theta();
				logLikeliHood = calculateLogLikelyHood();
				Step++;
				
			}while(Step<10/*!(converged(logLikeliHood, prevLogLikeliHood)*/);
			
			
			System.out.println("Modified");
			
			System.out.println("*****************************************");
			
		}catch(IFException e){
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
