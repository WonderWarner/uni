/* 
 * "Small Hello World" example. 
 * 
 * This example prints 'Hello from Nios II' to the STDOUT stream. It runs on
 * the Nios II 'standard', 'full_featured', 'fast', and 'low_cost' example 
 * designs. It requires a STDOUT  device in your system's hardware. 
 *
 * The purpose of this example is to demonstrate the smallest possible Hello 
 * World application, using the Nios II HAL library.  The memory footprint
 * of this hosted application is ~332 bytes by default using the standard 
 * reference design.  For a more fully featured Hello World application
 * example, see the example titled "Hello World".
 *
 * The memory footprint of this example has been reduced by making the
 * following changes to the normal "Hello World" example.
 * Check in the Nios II Software Developers Manual for a more complete 
 * description.
 * 
 * In the SW Application project (small_hello_world):
 *
 *  - In the C/C++ Build page
 * 
 *    - Set the Optimization Level to -Os
 * 
 * In System Library project (small_hello_world_syslib):
 *  - In the C/C++ Build page
 * 
 *    - Set the Optimization Level to -Os
 * 
 *    - Define the preprocessor option ALT_NO_INSTRUCTION_EMULATION 
 *      This removes software exception handling, which means that you cannot 
 *      run code compiled for Nios II cpu with a hardware multiplier on a core 
 *      without a the multiply unit. Check the Nios II Software Developers 
 *      Manual for more details.
 *
 *  - In the System Library page:
 *    - Set Periodic system timer and Timestamp timer to none
 *      This prevents the automatic inclusion of the timer driver.
 *
 *    - Set Max file descriptors to 4
 *      This reduces the size of the file handle pool.
 *
 *    - Check Main function does not exit
 *    - Uncheck Clean exit (flush buffers)
 *      This removes the unneeded call to exit when main returns, since it
 *      won't.
 *
 *    - Check Don't use C++
 *      This builds without the C++ support code.
 *
 *    - Check Small C library
 *      This uses a reduced functionality C library, which lacks  
 *      support for buffering, file IO, floating point and getch(), etc. 
 *      Check the Nios II Software Developers Manual for a complete list.
 *
 *    - Check Reduced device drivers
 *      This uses reduced functionality drivers if they're available. For the
 *      standard design this means you get polled UART and JTAG UART drivers,
 *      no support for the LCD driver and you lose the ability to program 
 *      CFI compliant flash devices.
 *
 *    - Check Access device drivers directly
 *      This bypasses the device file system to access device drivers directly.
 *      This eliminates the space required for the device file system services.
 *      It also provides a HAL version of libc services that access the drivers
 *      directly, further reducing space. Only a limited number of libc
 *      functions are available in this configuration.
 *
 *    - Use ALT versions of stdio routines:
 *
 *           Function                  Description
 *        ===============  =====================================
 *        alt_printf       Only supports %s, %x, and %c ( < 1 Kbyte)
 *        alt_putstr       Smaller overhead than puts with direct drivers
 *                         Note this function doesn't add a newline.
 *        alt_putchar      Smaller overhead than putchar with direct drivers
 *        alt_getchar      Smaller overhead than getchar with direct drivers
 *
 */

#include "sys/alt_stdio.h"
#include "system.h"
#include "altera_avalon_pio_regs.h"

// Vezerlobitek beallitasa
#define RESET (1 << 0)
#define SWITCH_MODE (1 << 1)
#define SET_TIME (1 << 2)

// Reset
void reset_time() {
	IOWR_ALTERA_AVALON_PIO_CLEAR_BITS(STATE_SETUP_BASE, RESET);
	usleep(100);
	IOWR_ALTERA_AVALON_PIO_SET_BITS(STATE_SETUP_BASE, RESET);
}

// Nezet valtasa
void switch_mode() {
	IOWR_ALTERA_AVALON_PIO_SET_BITS(STATE_SETUP_BASE, SWITCH_MODE);
	usleep(100);
	IOWR_ALTERA_AVALON_PIO_CLEAR_BITS(STATE_SETUP_BASE, SWITCH_MODE);
}

// Ido beallitasa
void set_time(unsigned int time) {
	unsigned int hh = time / 1000000;
	unsigned int mm = (time / 10000) % 100;
	unsigned int ss = (time / 100) % 100;

	if(hh > 23 || mm > 59 || ss > 59) {
		alt_printf("Invalid time format!\n");
		return;
	}

	IOWR_ALTERA_AVALON_PIO_DATA(TIME_SETUP_BASE, time);
	IOWR_ALTERA_AVALON_PIO_SET_BITS(STATE_SETUP_BASE, SET_TIME);
	usleep(15000);
	IOWR_ALTERA_AVALON_PIO_CLEAR_BITS(STATE_SETUP_BASE, SET_TIME);
}

// Ido megjelenitese
void read_time() {
	unsigned int time = IORD_ALTERA_AVALON_PIO_DATA(TIME_COUNTER_BASE);
	printf("%02u:%02u\n", time/100, time%100);
}

// Ido megjelenitese adott ideig ms-ben
void read_for(unsigned int duration) {

	const unsigned int MAX_DURATION = 1000 * 60 * 60 * 2;
	if(duration > MAX_DURATION) {
		alt_printf("Duration too long. Max = 7200000 ms (2 hours).\n");
		return;
	}

	const unsigned int update_interval = 100;
	unsigned int elapsed = 0;

	while(elapsed < duration) {
		read_time();
		usleep(update_interval * 1000); // microsec
		elapsed += update_interval;
	}
}

// Scenario
// 100 ms, Read, Time reset, Read for 100 ms, Mode switch, Read for 1500 ms, time set to 23593030, Read for 10000 ms, Mode Switch, Read for 25000 ms
void demo() {
	alt_printf("Demo started...\n");

	usleep(100 * 1000);
	read_time();
	reset_time();
	read_for(100);
	switch_mode();
	read_for(1500);
	set_time(23593030);
	read_for(10000);
	switch_mode();
	read_for(25000);

	alt_printf("Demo finished.\n");
}

int read_line(char* buffer) {
	int i = 0;
	char c;

	while(1) {
		c = alt_getchar();
		if(c == '\n' || c == '\r') {
			break;
		}
		if(i < 63) {
			buffer[i++] = c;
		}
	}

	buffer[i] = '\0';
	return i;
}

unsigned int starts_with(const char* a, const char* b) {
	while(*b != '\0') {
		if(*a == '\0') return 0;
		if(*a++ != *b++) return 0;
	}
	return 1;
}

unsigned int read_num(const char* str) {
	unsigned int val = 0;
	int i = 0;
	for(i=0; str[i] != '\0'; i++) {
		if(str[i] >= '0' && str[i] <= '9') {
			val = val * 10 + (str[i] - '0');
		} else return -1;
	}
	printf("%u", val);
	return val;
}

int main() {
	IOWR_ALTERA_AVALON_PIO_SET_BITS(STATE_SETUP_BASE, RESET);

	char cmd[64];

	alt_printf("Welcome to the RTC demo of Peter Tomori!\n");
	alt_printf("Available commands:\n");
	alt_printf("reset -> Reset the clock to 00:00:00:00\n");
	alt_printf("mode -> Switch display mode between hh:mm and ss:cc\n");
	alt_printf("set <hhmmsscc> -> Set time to hh:mm:ss:cc (e.g., 12345678 -> 12:34:56:78)\n");
	alt_printf("read -> Display time in hh:mm or ss:cc based on the applied mode\n");
	alt_printf("readfor <t> -> Displays time for t milliseconds.");
	alt_printf("demo -> Shows demonstration of the clock\n");

	while(1) {
		alt_printf("> ");
		if(!read_line(cmd)) continue;

		if(starts_with(cmd, "reset")) {
			reset_time();
		}
		else if(starts_with(cmd, "mode")) {
			switch_mode();
		}
		else if(starts_with(cmd, "set ")) {
			unsigned int t = read_num(cmd + 4);
			if(t != -1) {
				set_time(t);
			}
			else alt_printf("Invalid number.\n");
		}
		else if(starts_with(cmd, "read")) {
			read_time();
		}
		else if(starts_with(cmd, "readfor ")) {
			unsigned int dur = read_num(cmd + 8);
			if(dur != -1) {
				read_for(dur);
			}
			else alt_printf("Invalid number.\n");
		}
		else {
			alt_printf("Unknown command.\n");
		}
	}

	return 0;

}
