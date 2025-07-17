library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity rtc_counter is
    port (
        clk_100Hz : in  std_logic;
        reset_n   : in  std_logic;
		  set_enable: in	std_logic;
		  set_hh		: in  std_logic_vector(5 downto 0);
		  set_mm		: in  std_logic_vector(5 downto 0);
		  set_ss		: in  std_logic_vector(5 downto 0);
		  set_cc		: in  std_logic_vector(6 downto 0);
        hh        : out std_logic_vector(5 downto 0); -- 0-23
        mm        : out std_logic_vector(5 downto 0); -- 0-59
        ss        : out std_logic_vector(5 downto 0); -- 0-59
        cc        : out std_logic_vector(6 downto 0)  -- 0-99
    );
end entity rtc_counter;

architecture rtl of rtc_counter is
    signal s_cc : integer range 0 to 99 := 0;
    signal s_ss : integer range 0 to 59 := 0;
    signal s_mm : integer range 0 to 59 := 0;
    signal s_hh : integer range 0 to 23 := 0;
begin

    process(clk_100Hz, reset_n, set_enable, set_hh, set_mm, set_ss, set_cc)
    begin
        if reset_n = '0' then
            s_cc <= 0; s_ss <= 0; s_mm <= 0; s_hh <= 0;
        elsif rising_edge(clk_100Hz) then
				if set_enable = '1' then
					s_hh <= to_integer(unsigned(set_hh));
					s_mm <= to_integer(unsigned(set_mm));
					s_ss <= to_integer(unsigned(set_ss));
					s_cc <= to_integer(unsigned(set_cc));
            elsif s_cc = 99 then
                s_cc <= 0;
                if s_ss = 59 then
                    s_ss <= 0;
                    if s_mm = 59 then
                        s_mm <= 0;
                        if s_hh = 23 then
                            s_hh <= 0;
                        else
                            s_hh <= s_hh + 1;
                        end if;
                    else
                        s_mm <= s_mm + 1;
                    end if;
                else
                    s_ss <= s_ss + 1;
                end if;
            else
                s_cc <= s_cc + 1;
            end if;
        end if;
    end process;
    
    -- assign es konverzio
    hh <= std_logic_vector(to_unsigned(s_hh, hh'length));
    mm <= std_logic_vector(to_unsigned(s_mm, mm'length));
    ss <= std_logic_vector(to_unsigned(s_ss, ss'length));
    cc <= std_logic_vector(to_unsigned(s_cc, cc'length));

end architecture rtl;