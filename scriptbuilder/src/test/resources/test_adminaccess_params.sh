cat > /etc/sudoers <<-'END_OF_JCLOUDS_FILE'
	Defaults    env_reset
	Defaults    secure_path="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
	root ALL = (ALL) ALL
	%wheel ALL = (ALL) NOPASSWD:ALL
END_OF_JCLOUDS_FILE
chmod 0440 /etc/sudoers
mkdir -p /over/ridden
chmod 0755 /over/ridden
getent group wheel || groupadd -f wheel
useradd -c 'foo' -s /bin/bash -g wheel -m  -d /over/ridden/foo -p 'crypt(bar)' foo
mkdir -p /over/ridden/foo/.ssh
cat >> /over/ridden/foo/.ssh/authorized_keys <<-'END_OF_JCLOUDS_FILE'
	fooPublicKey
END_OF_JCLOUDS_FILE
chmod 600 /over/ridden/foo/.ssh/authorized_keys
chown -R foo /over/ridden/foo/.ssh
chown -R foo /over/ridden/foo
exec 3<> /etc/ssh/sshd_config && awk -v TEXT="PasswordAuthentication no
PermitRootLogin no
" 'BEGIN {print TEXT}{print}' /etc/ssh/sshd_config >&3
hash service 2>&- && service ssh reload 2>&- || service sshd reload 2>&- || /etc/init.d/ssh* reload
awk -v user=^${SUDO_USER:=${USER}}: -v password='crypt(0)' 'BEGIN { FS=OFS=":" } $0 ~ user { $2 = password } 1' /etc/shadow >/etc/shadow.${SUDO_USER:=${USER}}
test -f /etc/shadow.${SUDO_USER:=${USER}} && mv /etc/shadow.${SUDO_USER:=${USER}} /etc/shadow
