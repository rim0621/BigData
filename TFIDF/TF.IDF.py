#-*- coding: utf-8 -*-

import numpy as np
import glob
import sys

def Word_table(f):
    data=f.read().split()
    table=[]
    count=[]
    table.append(data[0])
    count.append(1)
    n=1
    for i in range(1,len(data)):
        for k in range(0,n):
            if data[i]!=table[k]:
                if k==n-1:
                    table.append(data[i])
                    count.append(1)
                    n=n+1
                    break
                continue
            elif data[i]==table[k]:
                count[k]=count[k]+1
                break


    return table,count

def IDF_n(word,word_input):
    count=0
    for i in range(0,len(word)):
        for k in range(0,len(word[i][0])):
            if word_input==word[i][0][k]:
                count+=1
                break

    return count

def IDF(word,word_input):


    return np.log2(len(word)/IDF_n(word,word_input))



def TF(word,number,word_input):
    f_word=0
    f_max_word=0
    #print (word[number][0])
    for i in range(0,len(word[number][0])):
        if word[number][0][i]==word_input:
            f_word=word[number][1][i]
            break
    for i in range(0,len(word)):
        for j in range(0,len(word[i][0])):
            if word[i][0][j]==word_input:
                if f_max_word<word[i][1][j]:
                    f_max_word=word[i][1][j]
                    break

    return f_word/f_max_word



def main(arvg):

    number_input=input("what number of text file? ")
    number=int(number_input) -1
    word_input=input("word? ")
    word=[]
    flist=glob.glob("*.txt")    #모든 txt파일 불러오기

    for fname in flist:                     #word 1차는 한파일 단어들 2차는 한파일의 단어랑 갯수 3차는 각각
        f=open(fname,'r',encoding='utf-8')
        word.append(Word_table(f))


    print("TF= ",TF(word,number,word_input),"IDF= ",IDF(word,word_input))
    result=TF(word,number,word_input)*IDF(word,word_input)

    print(result)


if __name__=='__main__':
    main(sys.argv)


