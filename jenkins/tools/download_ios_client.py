#!/usr/bin/env python

import os, errno
import sys
import argparse
import subprocess
import os.path
import shutil
import re


def main():

    build_number_file_name = "ZClient_ios_build.txt"

    if len(sys.argv) < 2:
        print "No parameters received.\nUsing default parameters: latest s3://z-lohika/ios/dev/ --path ./"
        build_num = "latest"
        sys.argv = ["", "latest", "s3://z-lohika/ios/dev/", "--path", "./"]

    if len(sys.argv) == 2:
        print "Only build number parameter received.\nDownloading build {0} with default parameters: {0} s3://z-lohika/ios/dev/ --path ./".format(sys.argv[1])
        build_num = sys.argv[1]
        sys.argv = ["", build_num, "s3://z-lohika/ios/dev/", "--path", "./"]

    if len(sys.argv) == 4:
        print "Only build number and path parameters received.\nDownloading build {0} to {1}".format(sys.argv[1], sys.argv[3])
        build_num = sys.argv[1]
        path = sys.argv[3]
        sys.argv = ["", build_num, "s3://z-lohika/ios/dev/", "--path", path]

    parser = argparse.ArgumentParser(description="Download latest client")
    parser.add_argument('build_num', help="The build to download, set it to  \"latest\" to get the last one")
    parser.add_argument('bucket_path', help="The S3 path to search for releases")
    parser.add_argument('--path', help="Where to store unpacked app")

    options = parser.parse_args()
    build_num = options.build_num
    bucket_path = options.bucket_path
    if not bucket_path.startswith("s3://"):
        print "Error: Bucket name needs to start with s3://"
        sys.exit(1)

    if options.path:
        store_path = options.path
        build_number_file_name=store_path+"/"+build_number_file_name
    else:
        store_path = "./"

    if bucket_path[-1] != "/":
        bucket_path += "/"

    if build_num == "latest":
        remote_file_name = get_latest_app(bucket_path)
        latest_build_number = get_latest_build_number(bucket_path)
    else:
        remote_file_name = get_specific_app(bucket_path, build_num)
        latest_build_number = build_num

    local_file_name = "ZClient_ios.app"
    #local_file_name = remote_file_name[:-(len(".cpio"))]
    local_file_name=store_path+"/"+local_file_name

    if os.path.exists(local_file_name):
        shutil.rmtree(local_file_name)

    full_remote_file_name = bucket_path + remote_file_name

    command = "/usr/local/bin/aws s3 cp {0} {1}/~app_ios_download_temp.cpio".format(full_remote_file_name, store_path)
    subprocess.call(command, shell=True)

    mkdir_p(local_file_name)

    if store_path == "./":
        extract_command = "pushd \"{0}\"; cpio -id < .{1}~app_ios_download_temp.cpio; popd".format(local_file_name, store_path)
    else:
        extract_command = "pushd \"{0}\"; cpio -id < {1}/~app_ios_download_temp.cpio; popd".format(local_file_name, store_path)
    subprocess.call(extract_command, shell=True)

    save_build_number(latest_build_number, build_number_file_name)
    
    if build_num != "latest":
        local_file_name_with_build = local_file_name[:-(len(".app"))]+"+{0}.app".format(latest_build_number)
        print "Saving additional file with name: {0}".format(local_file_name_with_build)
        prepare_command = "rm -rf {0}".format(local_file_name_with_build)
        subprocess.call(prepare_command, shell=True)
        copy_command = "cp -a {0} {1}".format(local_file_name, local_file_name_with_build)
        subprocess.call(copy_command, shell=True)

    if os.path.exists(store_path+"/~app_ios_download_temp.cpio"):
        os.remove(store_path+"/~app_ios_download_temp.cpio")

def get_latest_app(bucket_path):
    command = "/usr/local/bin/aws s3 ls {0}".format(bucket_path)
    listing =  subprocess.check_output(command, shell=True)
    listing = listing.splitlines()

    apps = [l.split()[3] for l in listing if l.find("simulator") != -1 and l.endswith(".app.cpio")]

    if len(apps) == 0:
      print "Error: No simulator apps found in {0}".format(bucket_path)
      sys.exit(1)

    return apps[-1]

def get_specific_app(bucket_path, build_num):
    command = "/usr/local/bin/aws s3 ls {0}".format(bucket_path)
    listing =  subprocess.check_output(command, shell=True)
    listing = listing.splitlines()

    apps = [l.split()[3] for l in listing if l.find("simulator") != -1 and l.find(build_num) != -1 and l.endswith(".app.cpio")]

    if len(apps) == 0:
      print "Error: Simulator build {0} is not found in {1}".format(build_num, bucket_path)
      sys.exit(1)
    print "\nDefined file to download: "+apps[-1]
    return apps[-1]

def get_latest_build_number(bucket_path):
  latest_cpio = get_latest_app(bucket_path)
  print "\nLatest file to download: "+latest_cpio

  if not latest_cpio:
    print "Warning: Bucket {0} seems to be empty".format(bucket_path)
    exit(-1)

  build_number_match = re.search("(\d+).app.cpio", latest_cpio)

  if not build_number_match:
    print "Error: Unable to get the build number from {0}".format(latest_cpio)
    exit(-1)

  build_number = int( build_number_match.groups()[0] )

  return build_number
  
def save_build_number(build_number, build_number_file_name):
  with open(build_number_file_name, 'w') as f:
    build_number_string = str(build_number)
    print "Saving to txt file build number: {0}".format(build_number)
    f.write(build_number_string)

def mkdir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc: # Python >2.5
        if exc.errno == errno.EEXIST and os.path.isdir(path):
            pass
        else: raise

if __name__ == "__main__":
    main()
