package com.jabuya.system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JabuyaApplicationTests {

	@Test
	void contextLoads() {

	}

	public static void main(String[] args) {
		int n=6;
		// Write your code here
		for( int i=1;i<=n;i++){
			int padding = ((n - i) / 2)+i;
			for(int j=1;j<=i;j++){
				String result="";
				if(j==1&&padding>0){
					String format="%"+padding+"s";
					//System.out.print(format);
				result= String.format(format,"#");
				}else {
					result=("#");
				}
				System.out.print(result.replace("'",""));
			}
			System.out.print("\n");
		}
	}

}
