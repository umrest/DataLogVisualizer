#include <iostream>
#include <fstream>
#include <ostream>
#include <time.h>

int main() {
	using namespace std;




	ofstream outfile;

	outfile.open("exampleData.REST_DATA");

	outfile << "#<DATUM_ID,CAN_ID,PERCENT_VBUS,CURRENT_DRAW,ENCODER_POS,ENCODER_VEL>\n";
	for (int can = 1; can < 53; can++) {
		for (int i = 0; i < 100; i++) {
			outfile << "M," << can << "," << (i / 10) << "," << + i * can << "," << i << "," << i << '\n';
		}
	}


	outfile.close();
	return 0;
}