library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity rtc_top is
    port (
        clk     : in  std_logic;
        reset_n : in  std_logic;
        btn     : in  std_logic;
        HEX0    : out std_logic_vector(6 downto 0);
        HEX1    : out std_logic_vector(6 downto 0);
        HEX2    : out std_logic_vector(6 downto 0);
        HEX3    : out std_logic_vector(6 downto 0)
    );
end entity;

architecture rtl of rtc_top is

    -- Komponensek deklaracioja
    
    --- Orajeloszto
    component clk_divider
        port (
            clk     : in  std_logic;
            reset_n : in  std_logic;
            clk_out : out std_logic
        );
    end component;

    --- Ido szamlalo
    component rtc_counter
        port (
            clk_100Hz : in  std_logic;
            reset_n   : in  std_logic;
				set_enable: in	 std_logic;
				set_hh	 : in  std_logic_vector(5 downto 0);
				set_mm  	 : in  std_logic_vector(5 downto 0);
				set_ss	 : in  std_logic_vector(5 downto 0);
				set_cc	 : in  std_logic_vector(6 downto 0);
            hh        : out std_logic_vector(5 downto 0);
            mm        : out std_logic_vector(5 downto 0);
            ss        : out std_logic_vector(5 downto 0);
            cc        : out std_logic_vector(6 downto 0)
        );
    end component;
    
    --- Hetszegmenses kielzo
    component hex
        port (
            input  : in  std_logic_vector(3 downto 0);
            output : out std_logic_vector(6 downto 0)
        );
    end component;
	 
	 -- Processzor
    component nios_system is
        port (
            clk_clk             : in  std_logic                     := 'X';             -- clk
            reset_reset_n       : in  std_logic                     := 'X';             -- reset_n
            state_setup_export  : out std_logic_vector(2 downto 0);                     -- export
            time_setup_export   : out std_logic_vector(24 downto 0);                    -- export
            time_counter_export : in  std_logic_vector(24 downto 0) := (others => 'X')  -- export
        );
    end component nios_system;

    -- Belso valtozok
    signal clk_100Hz : std_logic; -- orajel
    
    signal hh, mm, ss : std_logic_vector(5 downto 0); -- ido
    signal cc : std_logic_vector(6 downto 0);

    type state_type is (SHOW_TIME, SHOW_SEC); -- allapotgep
    signal state : state_type := SHOW_TIME; -- allapot
    signal btn_prev : std_logic := '0'; -- gomb elozo allapota

    signal dig0, dig1, dig2, dig3 : std_logic_vector(3 downto 0); -- megjelenitett ertekek
	 
	 signal reset_internal : std_logic; -- vagy az fpga reset gombja vagy a processzor reset jele
	 
	 -- Processzor-RTC interfesz
	 signal control : std_logic_vector(2 downto 0);  -- bit0 = reset, bit1 = state mode, bit2 = set time
	 signal time_setup   : std_logic_vector(24 downto 0); -- ido beallitas
	 signal time_output  : std_logic_vector(24 downto 0); -- ido visszakuldes
	 
begin
    -- Peldanyositas
	 u0 : component nios_system
        port map (
            clk_clk             => clk,            -- clk.clk
            reset_reset_n       => reset_n,       	-- reset.reset_n
            state_setup_export  => control,  		-- state_setup.export
            time_setup_export   => time_setup,   	-- time_setup.export
            time_counter_export => time_output  	-- time_counter.export
        );
	
	 reset_internal <= reset_n and control(0);
	 
    U_DIV: clk_divider
        port map (clk, reset_internal, clk_100Hz);
		  
	
    U_RTC: rtc_counter
        port map (clk_100Hz, reset_internal, control(2),
						time_setup(24 downto 19), time_setup(18 downto 13), time_setup(12 downto 7), time_setup(6 downto 0),
						hh, mm, ss, cc);

    -- Allapotgep (gomb pozitiv elere valt)
    process(clk)
    begin
        if rising_edge(clk) then
            if reset_internal = '0' then
                state <= SHOW_TIME;
                btn_prev <= '0';
            else
                if (btn = '1' and btn_prev = '0') or control(1) = '1'  then -- le lett nyomva vagy jel lett küldve
                    if state = SHOW_TIME then
                        state <= SHOW_SEC;
                    else
                        state <= SHOW_TIME;
                    end if;
                end if;
                btn_prev <= btn;
            end if;
        end if;
    end process;
	 
	 

    -- Kijelzo ertekei
    process(state, hh, mm, ss, cc)
        variable h_int : integer;
        variable m_int : integer;
        variable s_int : integer;
        variable c_int : integer;
    begin
        case state is
            when SHOW_TIME =>
                h_int := to_integer(unsigned(hh));
                m_int := to_integer(unsigned(mm));
            
                dig3 <= std_logic_vector(to_unsigned(h_int / 10, 4));
                dig2 <= std_logic_vector(to_unsigned(h_int mod 10, 4));
                dig1 <= std_logic_vector(to_unsigned(m_int / 10, 4));
                dig0 <= std_logic_vector(to_unsigned(m_int mod 10, 4));
					 
					 time_output <= std_logic_vector(to_unsigned(h_int * 100 + m_int, 25));
            when SHOW_SEC =>
                s_int := to_integer(unsigned(ss));
                c_int := to_integer(unsigned(cc));
            
                dig3 <= std_logic_vector(to_unsigned(s_int / 10, 4));
                dig2 <= std_logic_vector(to_unsigned(s_int mod 10, 4));
                dig1 <= std_logic_vector(to_unsigned(c_int / 10, 4));
                dig0 <= std_logic_vector(to_unsigned(c_int mod 10, 4));
					 
					 time_output <= std_logic_vector(to_unsigned(s_int * 100 + c_int, 25));
        end case;
    end process;

    -- Szegmensek ertekei
    HEX_INST0: hex port map(input => dig0, output => HEX0);
    HEX_INST1: hex port map(input => dig1, output => HEX1);
    HEX_INST2: hex port map(input => dig2, output => HEX2);
    HEX_INST3: hex port map(input => dig3, output => HEX3);

end architecture;

