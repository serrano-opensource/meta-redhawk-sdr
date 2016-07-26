#
# This file is protected by Copyright. Please refer to the COPYRIGHT file distributed 
# with this source distribution.
#
# This file is part of Geon Technology's meta-redhawk-sdr.
#
# Geon Technology's meta-redhawk-sdr is free software: you can redistribute it and/or 
# modify it under the terms of the GNU Lesser General Public License as published by 
# the Free Software Foundation, either version 3 of the License, or (at your option) 
# any later version.
#
# Geon Technology's meta-redhawk-sdr is distributed in the hope that it will be useful, 
# but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
# details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with this program.  If not, see http://www.gnu.org/licenses/.
#

DESCRIPTION = "REDHAWK Framework FrontEnd Interfaces"
HOMEPAGE = "http://www.redhawksdr.org"
LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e6a600fd5e1d9cbde2d983680233ad02"

DEPENDS = "redhawk-bulkio"
RDEPENDS_${PN} = "redhawk-bulkio"

PREFERRED_VERSION_redhawk-bulkio = "2.%"

SRC_URI = "\
    git://github.com/RedhawkSDR/frontendInterfaces.git;tag=2.3.1;protocol=git \
    file://IDLDIR.patch \
    file://Add_Missing_Files.patch \
"

S = "${WORKDIR}/git"

# We have to inherit from pythonnative if we do stuff with the system python.
# autotools-brokensep is the sasme as autotools but our build and src locations are the same since we cannot build away from our src.
inherit autotools-brokensep pkgconfig pythonnative redhawk-sysroot redhawk-oeconf


FILES_${PN} += " \
    ${OSSIEHOME}/* \
"
INSANE_SKIP_${PN} += "debug-files dev-so staticdev libdir"


EXTRA_OECONF += "\
    --disable-java \
    --disable-log4cxx \
    "
EXTRA_AUTORECONF += "-I ${OSSIEHOME_STAGED}/share/aclocal/ossie"

# Required
CXXFLAGS += "-fpermissive"
CFLAGS += "-fpermissive"

# Since prefix is set this has to override that
CACHED_CONFIGUREVARS += "ac_cv_pymod_ossie=yes ac_cv_pymod_bulkio_bulkioInterfaces=yes"

# Needed so that when the python distutils is run it can get the system prefix which, since it's the build system python will be /.../x86_64-linux/usr and replace it with our host systems name.
do_configure_prepend() {
  export BUILD_SYS=${BUILD_SYS}
  export HOST_SYS=${HOST_SYS}
  export STAGING_INCDIR=${STAGING_INCDIR}
  export STAGING_LIBDIR=${STAGING_LIBDIR}
  export STAGING_BASE=${STAGING_DIR}/${MACHINE}
  export PKG_CONFIG_PATH="${OSSIEHOME_STAGED}/lib/pkgconfig:${PKG_CONFIG_PATH}"
}

# Needed so that when the python distutils is run it can get the system prefix.
do_install_prepend() {
  export BUILD_SYS=${BUILD_SYS}
  export HOST_SYS=${HOST_SYS}
  export STAGING_INCDIR=${STAGING_INCDIR}
  export STAGING_LIBDIR=${STAGING_LIBDIR}
}