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
	public static HashMap<String,Integer> maps = new HashMap<String,Integer>();//mapΪ�������Ժ�index��ӳ���
	public static ArrayList<HashMap> combine = new ArrayList<HashMap>();//combineΪ���и��ڵ���Լ������
	public static String[][] or_data= new String[11100][37];//�����������
	public static ArrayList<ArrayList<Double>> theta=new ArrayList<ArrayList<Double>>();
	public static HashMap[] attribute_value_num = new HashMap[37];//ÿһ����һ��map(���Զ�Ӧ��value T F��value����Ŀ)
	public static ArrayList<ArrayList<Double>> pro= new ArrayList<ArrayList<Double>>();//��Ÿ��ڵ�������ʣ�ÿһ����һ�����ԣ�ÿһ�ж�Ӧÿһ��value�ĸ���
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
	public static void read_data() //��������ŵ�records��
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

	
	public static void calculate_num() //����ÿһ�г�ȥ������Ŀ
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
				//ͳ��ÿ�����Ե�value����Ŀ
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
				//���map ����{High=3201, Low=3378, Normal=4249, ?=272}
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
				
				//����ÿ�����Եĸ���(pro[i])
				for (int i=0; i<37; i++) {
					InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
					//û�и��ڵ�
					if(n.get_parents().isEmpty()) 
					{
						ArrayList<Integer> num = new ArrayList<Integer>();//���value����Ŀ
						
						//�õ���������
//						for(Iterator<Map.Entry<String, Integer>> it = attribute_value_num[i].entrySet().iterator(); it.hasNext();) 
//						{
//							System.out.print("11");
//							Map.Entry<String, Integer> entry = it.next();
//							Integer value = entry.getValue();
//							num.add(value);
//						}
						
						
				        //��for each����
						
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
						ArrayList<Double> pro_row = new ArrayList<Double>();//���һ�����Ե�value����
						for(Iterator it = num.iterator();it.hasNext();j++) 
						{
							int cur_num = (int) it.next();//��ǰvalue����Ŀ
							double probability = (double)cur_num/sum;
							pro_row.add(( Double ) probability);
						}
						pro.add(pro_row);
						//����������
//						for(Double d : pro_row)
//						{
//							System.out.println(d);
//						}				
					 } 
					
					else //�и��ڵ�
					{
						//���и��ڵ�ĸ���
					}
				}
				//����������  ��������
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
				//System.out.println(names.get(i));//����
				ArrayList<String> list=new ArrayList<String>();
				String[] temp = n.get_values();//read records in lines
				for(int j=0;j<temp.length;j++){
					list.add(temp[j]);
					//System.out.print(list.get(j));//ֵ
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
			ArrayList<String> row=new ArrayList<String>();//����ڼ��е�index
			for(int i=0;i<size;i++){//11100
				ArrayList<ArrayList<String>> list_temp0 = new ArrayList<ArrayList<String>>();//�洢ÿ�����ӵļ�¼
				ArrayList<String> listi=new ArrayList<String>();//��ȡ����i��
				listi = re_data.get(i);
				ArrayList<String> value=new ArrayList<String>();//�洢���ʺ��Ǹ�Ԫ�ض�Ӧ��ֵ
				int len=0;//�洢ÿ��Ҫ���Ӷ��ټ�¼
				int t=0;//֪��Ҫ���ڼ���Ԫ��
				
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
				String listij=listi.get(t);//��ȡ����ij��Ԫ��
				for (int k=0;k<len;k++){
					ArrayList<String> temp=new ArrayList<String>();//�洢ÿ��Ԫ��
					temp=listi;
					//System.out.println("111"+listij+"***"+re_value.get(t-1).get(k));
					temp.set(t,"\""+re_value.get(t-1).get(k)+"\"");
					temp.set(39,";"+Integer.toString(t));
					list_temp0.add(k,(ArrayList<String>)temp.clone());//��Ҫ��ӵ�������ӵ�һ�����Arraylist����
					
				}
				re_data.addAll(list_temp0);//�ϲ��������
				
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
	
	public static void combine() {//��¼���ڵ���Լ����������
		try {
			InferenceGraph G = new InferenceGraph("./data/alarm.bif");
			Vector nodes = G.get_nodes();
			for (int i=0; i<37; i++) 
			{
				InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
				String name = n.get_name();
				maps.put(name, (Integer)i);//��ÿ������ӳ��index
			}
			for(int i=0; i<37; i++)
			{
				InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
				HashMap<ArrayList,ArrayList> map = new HashMap<ArrayList,ArrayList>();//ÿ���ڵ�����һ��map(���ڵ�index�����ڵ����)
				if(n.get_parents().isEmpty())//û�и��ڵ�ֱ�Ӵ��Լ������
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
				else //�и��ڵ㣬�������ڵ�
				{
					Vector parents = n.get_parents();
					ArrayList<String[]> values = new ArrayList<String[]>();//��Ÿýڵ�ĸ��ڵ������values
					ArrayList<Integer> par_index = new ArrayList<Integer>();//��Ÿ��ڵ�INDEX���
					ArrayList<String> value = new ArrayList<String>();//��Ÿ��ڵ���������(�ַ���)���
					for(Enumeration e = parents.elements(); e.hasMoreElements();)
					{
						InferenceGraphNode no = (InferenceGraphNode)(e.nextElement());
						String[] s=no.get_values();//ÿ�����ڵ��value
						values.add(s);
						par_index.add(maps.get(no.get_name())) ;
					}
					par_index.add(maps.get(n.get_name()));
					//�����Լ���value
					String[] self_values = n.get_values();
					values.add(self_values);
					test(value,values,values.get(0),"");//��Ÿ��׽ڵ�values����n�����׽ڵ�value
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
	
	
	public static double calculate_probabilityij(int line,int row){//line�У�row��
		//������ʺŽڵ��index
		/*for(int i=0;i<re_data.size();i++){
			int num1=Integer.parseInt(re_data.get(i).get(39).substring(1, re_data.get(i).get(39).length()))-1;
		}*/
		double pro=0.0;
		try {
			//��������ʺŵ�index
			//int num1=Integer.parseInt(re_data.get(line).get(38).substring(1, re_data.get(line).get(38).length()))-1;
			row=row-1;//row is j,line is i
			InferenceGraph G = new InferenceGraph("./data/alarm.bif");
			Vector nodes = G.get_nodes();
			//int weight=0;
			//InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(num));
			//�ҳ�����n���ӽڵ�����
			//�ҳ�����n=value[i]�����
			
			//��P(x1,...,x37)��ȫ����
			//���ȱ���ÿ�����ԣ���Ӧ�Ҹ��ڵ㣬���ڵ�����ȥtheta�����Ҹ���
			/*for(int i=0;i<37;i++){*/
			ArrayList <String>combine_value=new ArrayList<String>();
			ArrayList /*<String>*/combine_key=new ArrayList/*<String>*/();
			//if(!((InferenceGraphNode)nodes.elementAt(row)).get_parents().isEmpty()){
				//�и��ڵ㡷���ݶ�Ӧ���ӽڵ��index��ȥ����Ϊ�ӽڵ�ĸ��ڵ����ϡ�
				HashMap <ArrayList,ArrayList> map = new HashMap<ArrayList,ArrayList>();
				map=combine.get(row);//����ÿ���ӽڵ��Ӧ��һ��map
				//ArrayList<String> key_index=new ArrayList<String>();
				
				//int child_index =Integer.parseInt(key_index.get(key_index.size()-1));
				//child_index�ӽڵ��index����ArrayList�е�index
				//����index�ҵ���Ӧ�ĸ��ڵ�����Լ������
				//Ȼ��ȥtheta�����ҵ���Ӧ�ĸ���
				
				for (Map.Entry entry : map.entrySet()) {  
			        Object key = entry.getKey();//key��һ��list 
			        Object value = entry.getValue();//valueҲ��һ��list
			        combine_key.addAll((ArrayList)key);
			        combine_value.addAll((ArrayList<String>)value);
				}
				ArrayList<ArrayList<String>> value_new=convert_map_value_to_arr(combine_value);
				
				//System.out.println(value_new);
				//�Ҽ�¼�ж�Ӧindex��ȡֵ,index��combine_key��ֵ
				ArrayList <String> record_i_value=new ArrayList<String>();
				
				for(int m=0;m<combine_key.size();m++){//index�ĸ���
					//System.out.println("num="+(Integer.parseInt(combine_key.get(m).toString())+1));
					int num=Integer.parseInt(combine_key.get(m).toString())+1;
					//System.out.println(num);
					record_i_value.add((re_data.get(line).get(num).substring(1, re_data.get(line).get(num).length()-1)));
					//System.out.println(re_data.get(line).get(num));
					
				}
				//System.out.println("value_new"+value_new);
				//System.out.println("record_i_value"+record_i_value);
				//ȥƥ�䣬�õ�index
				int index=0;
				for(int n=0;n<value_new.size();n++){
					//System.out.println(value_new.get(n)+"="+record_i_value);
					if((value_new.get(n)).equals(record_i_value)){
						index=n;
						//System.out.println("n="+n);
					}
				}
				//����indexȥ��theta�е�row���ڵ��Ӧ��Arraylist��index��Ӧ�ĸ���
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
			int k=0;//��ʾ�ڼ���û�и��ڵ�Ľڵ�  ����Ӧpro ��Ϊproֻ����û�и��ڵ���������
			for(int i=0;i<37;i++){
				InferenceGraphNode n = ((InferenceGraphNode)nodes.elementAt(i));
				ArrayList<Double> row=new ArrayList<Double>();
				HashMap <ArrayList,ArrayList> map = new HashMap<ArrayList,ArrayList>();
				map=combine.get(i);//����ÿ���ӽڵ��Ӧ��һ��map
				//�õ�map�е�value(ArrayList)�ĸ������Ƕ�Ӧ��theta���и��ʵĸ�������Ӧ��index���Ƕ�Ӧ��theta����index�ĸ���
				//ȡ��map��value�Ĵ�С
				ArrayList combine_value=new ArrayList();
				for (Map.Entry entry : map.entrySet()) {  
			        Object value = entry.getValue();//key��һ��list 
			        combine_value.addAll((ArrayList)value);
				}
				//System.out.println("combine_value="+combine_value);
				int num=combine_value.size();//һ����num�����
//				System.out.println("num="+num);
				
				//�Ѷ�Ӧ�ĸ���ֵ���뵽��ʼ��theta����
				if(n.get_parents().isEmpty())
				{
					for (int j=0;j<num;j++)
					{
						row.add(pro.get(k).get(j));//�����Ӧ�ĸ���ֵ
					}
					k++;
				}
				else //�и��ڵ�Ľڵ�ĸ���ֱ�Ӿ���
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
		while(i<re_data.size()){//����ÿһ��
			int lines=0;//��һ���к���ͬ���Ǽ�
			re_data.get(i).get(0);//ÿһ�е��к�
			//�����ҵ��м����к���ͬ��
			ArrayList <Double>list=new ArrayList<Double>();//���ÿһ����Ե�ȫ����
			ArrayList <Double>list_all=new ArrayList<Double>();
			for(int j=i;j<re_data.size();j++){//j��i��ʼ��
				/*//��������ʺŵ�index
				String index_wenh=re_data.get(i).get(39);
				int num1=Integer.parseInt(index_wenh.substring(1, index_wenh.length()));*/
				//�ҵ���i���м�����Ե���
				if(re_data.get(i).get(0).equals(re_data.get(j).get(0))){
					lines=lines+1;
					double pro_i=calculate_TotalProbability_i(j);//ÿ�е�ȫ����
					list.add(pro_i);
				}
			}
			//System.out.println("list_��Ե�ȫ����"+list);
			double down=0.0;//һ�������ϵ�ȫ����֮��
			for(int m=0;m<list.size();m++){
				down+=list.get(m);
			}
			for(int n=0;n<list.size();n++){
				list_all.add(list.get(n)/down);//ÿһ�е�Ȩ��
			}
			
			for(int k=0;k<lines;k++){
				re_data.get(i+k).set(38,list_all.get(k).toString());
				re_data.get(i+k).set(40,list.get(k).toString());
			}
			i=i+lines;
			//System.out.println("list_all_��Ե�Ȩ��"+list_all);
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
			map=combine.get(i);//ÿ���ڵ�ĸ��ڵ���ϵ�map
			ArrayList combine_key=new ArrayList();
			ArrayList <String>combine_values=new ArrayList<String>();
			for (Map.Entry entry : map.entrySet())
			{  
		        Object key = entry.getKey(); //���ڵ�����index list
		        Object values = entry.getValue(); //���ڵ�����value list
		        combine_key.addAll((ArrayList)key);
		        combine_values.addAll((ArrayList<String>)values);
			}
			//System.out.println(i+"----------------------------begin");
			//System.out.println("combine_key="+combine_key);
			
			ArrayList<ArrayList<String>> value_new=convert_map_value_to_arr(combine_values);
			//System.out.println("combine_values="+value_new);
			
			int num = combine_key.size();
			int num2 = value_new.size();//�м������ڵ�����  ����TTF TTT �м���
			//System.out.println("keynum="+num);
			//System.out.println("value_new_num2="+num2);
			ArrayList<ArrayList<Double>> combine_weights=new ArrayList<ArrayList<Double>>();//������㸸�ڵ���ϵ�����Ȩ��
//			double weights =0;
			
			for(int k= 0;k<num2;k++)
			{//����ÿһ���ӽڵ��ÿһ���븸�ڵ�����
				ArrayList <String>values= value_new.get(k);//ÿһ��  ��  ��һ��arrylsit
				//ArrayList list = new ArrayList();//������� index�Ķ�Ӧ�е�ֵ
				ArrayList<Double> combine_weight=new ArrayList<Double>();//�����theta�ķ����ϵ�Ȩ��   ����TTT
				ArrayList<Double> combine_par_weight=new ArrayList<Double>();//�����theta�ķ�ĸ�ϵ�Ȩ��   ����TT
				//���ڵ���Լ���Ȩ��
				for(int line=0;line<re_data.size();line++)//ÿһ��
				{
					ArrayList list = new ArrayList();//ÿһ��newһ����������� index�Ķ�Ӧ�е�ֵ
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
				//ȥ���ӽڵ�ĸ��ڵ�
				
				double par_weight = 0.0;
				for(int line=0;line<re_data.size();line++)
				{	ArrayList copy_val=new ArrayList();
					copy_val.addAll(values);//copy values
					//System.out.println("2num="+num);
					//System.out.println("2num2="+num2);
					//ArrayList list = new ArrayList();//������� index�Ķ�Ӧ�е�ֵ
					ArrayList list1 = new ArrayList();//������� index�Ķ�Ӧ�е�ֵ
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
						list1.remove(list1.size()-1);//ȥ���Լ��ĸ��ڵ����
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
					String value = list.get(i).toString().trim().substring(1, list.get(i).length());//���ȥ��i��String
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
			
			int size=Integer.parseInt(re_data.get(i).get(39).substring(1,re_data.get(i).get(39).length()));//�õ������к�
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
			
			double prevLogLikeliHood = 0;//������Ȼ
			double logLikeliHood = 0;//������Ȼ
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
